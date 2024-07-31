# Elastic Job分布式定时任务管理系统框架


## 项目描述
  Elastic Job是一个分布式定时任务调度框架，旨在解决传统任务管理系统资源分配不均，大规模分布式环境中定时任务调度困难的问题。  

  Elastic Job提供即时、定时和每日三种任务类型，允许用户自主定义任务的执行方式。它能够动态增加、减少节点，并根据节点的负载情况智能分配任务，从而均衡使用各节点的资源。此外，Elastic Job 可以跟踪任务的执行状态及结果，并对执行失败的任务自动进行重试，确保任务最终完成。

## 使用说明
1. 配置好节点的configuration。  
2. 初始化Redis集群，并设置以 "cluster" 为key的分布式锁。
3. 在各个数据库中初始化好jobinfo和jobresult表，在primary数据库中额外增加一个sequence_table表，设置好可自增的PROCEDURE。
4. 修改以上在application.yml中的对应配置信息。
5. 根据任务需求对slaveService中的runJob方法进行重写。
6. 启动客户端连接至服务端，开始使用。
  
## 具体设计
  项目分为客户端和服务端，客户端提供增删改查接口供用户调用。服务端由主节点和从节点组成集群。
### 角色剖析
#### 1.Node
  Node是节点的初始状态，负责启动获取分布式锁的GetLock线程，并根据主从选举变更节点状态：主节点或从节点。
#### 2.MasterNode（主节点）
  MasterNode负责发布gRPC服务，向Redis集群中注册服务ip与port信息，响应客户端请求与子节点请求。
#### 3.MasterJobHandler
  MasterJobHandler是主节点的任务处理者，负责任务处理与调度。内部维护三个队列，waitingJob、preparedJob和runningJob。  
  waitingJob是一个优先级队列，存放还未到执行时间的任务，当到达任务执行时间时，任务会被移入preparedJob。  
  preparedJob中存放就是已经到达任务执行时间但是还未被分配执行的任务，只有当runningJob出现空余时，preparedJob中的任务才会被移入runningJob。  
  runningJob即是存放当前被分配出去执行的任务，runningJob的容量并不是由runningJob.size()决定的，而是根据各子节点注册时的maxParallel的综合决定，这样做可以避免任务被单节点过度消费。runningJob中的任务被执行完成后，队列会移出对应的任务。  
  MasterJobHandler对于任务的分配采用的是pull模型，也就是由子节点来发起获取任务的请求，MasterJobHandler根据请求中携带的资源数来按需分配。
#### 4.SlaveNode（子节点）
  SlaveNode负责与主节点交互，包括向主节点注册、注销、获取任务与转发添加任务请求。内部还有一个Producer线程，与SlaveJobHandler中的Consumer线程共享任务阻塞队列。当阻塞队列中没有任务时，主动向主节点拉取任务。
#### 5.SlaveJobHandler
  SlaveJobHandler负责任务消费，执行用户定义的runJob方法，并存储执行后的结果信息。内部有一个Consumer线程，负责消费任务和在没有任务时提醒Producer拉取任务。
### 模块剖析
#### 1.主从选举
  节点初次启动是Node状态，会启动一个GetLock线程拉取分布式锁。若获取到锁，则以主节点模式启动；若没有获取到锁，则以从节点模式启动。GetLock线程会每隔100ms拉取一次分布式锁，间隔时间可以根据系统可用性的粒度来决定。后续节点再获取到锁，会比较redis中存放的ip+port与自身是否相同，相同则代表重复加锁，仍是主节点模式。不同则要根据先前的状态信息来判断是主节点退化为子节点，还是依旧是子节点。
#### 2.Elastic集群搭建
  主节点启动后，会向Redis中存入key为 “host” value为自身ip:port的键值信息。子节点启动后，从Redis中获取到ip:port，向主节点发起注册请求。主节点判断子节点符合规范后，节点间建立连接。主节点宕机时，会自动释放分布式锁，这时获取到分布式锁的子节点会升级为主节点，接手上个主节点的业务执行，避免系统崩溃。
#### 3.任务类型
  任务分为三种类型：即时任务、定时任务和每日任务。即时任务支持用户设置任务执行的分钟数即几分钟后执行。定时任务支持用户设置任务执行的时间节点。每日任务支持用户设置任务每日的执行时间，任务在停用前会反复执行。
#### 4.任务执行流程
  客户端发送添加任务的请求到服务端后，MasterJobHandler会根据任务类型和调度参数计算出任务的预计执行时间，然后将任务存储在waitingJob中。当前时间到达任务的执行时间后，waitingJob中的任务会被移入preparedJob中，等待子节点发送获取任务的请求。主节点在收到获取任务的请求后，会根据请求中携带的资源信息按需分配给子节点，并将preparedJob中的任务移入runningJob。子节点收到任务后，将任务放入任务阻塞队列中，提醒SlaveJobHandler拉取任务执行。随后任务便会按照用户设定的runJob执行具体业务，执行完毕后直接向Jobresult表中插入对应的结果信息，而不需要回调结果请求。MasterJobHandler会不断地检查runningJob中的任务是否执行完成，执行完成的若不是每日任务则直接停用，若是则计算下次执行时间并放入waitingJob；执行失败的会申请重试，MasterJobHandler根据重试策略判断是否重试该任务。
#### 5.节点间通信
  主从节点间通信采用的是gRPC服务，考虑到gRPC优秀的性能而且可以使用Protobuf自动生成客户端和服务器端代码。
## 架构图
![_20240731115630.png](https://s2.loli.net/2024/07/31/duFc7AT3hIES5eM.png)
## 项目结构树   
![_20240731115823.png](https://s2.loli.net/2024/07/31/ZYxGrSPaN4XLTtR.png)