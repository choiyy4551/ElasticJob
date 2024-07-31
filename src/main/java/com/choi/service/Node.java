package com.choi.service;

import com.choi.pojo.Configuration;
import com.choi.pojo.JobInfo;
import com.choi.service.slave.SlaveJobHandler;
import com.choi.service.slave.SlaveNode;
import com.choi.service.master.MasterNode;
import com.choi.utils.MyUUID;
import lombok.Data;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Data
@Service
public class Node {
    private boolean hasLock = false;
    private Configuration configuration;
    private boolean shutDown;
    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MasterNode masterNode;
    @Autowired
    private SlaveNode slaveNode;
    @Autowired
    private SlaveJobHandler slaveJobHandler;
    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private MyUUID myUUID;
    private String nodeId;
    private Node node = this;
    private BlockingQueue<JobInfo> queue;
    private boolean statusChange = true;
    public void start(Configuration configuration) {
        this.configuration = configuration;
        nodeId = myUUID.createUUID();
        shutDown = false;
        masterNode.setConfiguration(configuration);
        slaveNode.setConfiguration(configuration);
        queue = new ArrayBlockingQueue<>(configuration.getMaxParallel());
        //创建获取集群锁的线程
        taskExecutor.submit(new GetLock());
        taskExecutor.submit(new ChangeStatus());
    }
    public void stop() {
        shutDown = true;
        slaveNode.stop();
    }
    //只有主节点才能添加任务，从节点只能将任务信息转发给主节点
    //主节点服务注册，可以避开从节点添加任务的问题
    public boolean addJob(JobInfo jobInfo) {
        if (hasLock) {
            System.out.println("主节点添加任务");
            return masterNode.addJob(jobInfo);
        } else {
            System.out.println("从节点添加任务");
            return slaveNode.addJob(jobInfo);
        }
    }
    //获取集群锁线程
    private class GetLock implements Runnable {
        @Override
        public void run() {
            String clusterName = configuration.getClusterName();
            while (!shutDown) {
                RLock lock = redissonClient.getLock(clusterName);
                if (hasLock) {
                    boolean isLock;
                    try {
                        isLock = lock.tryLock(1, 10, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (!isLock) {
                        //失去锁
                        hasLock = false;
                        statusChange = true;
                    } else {
                        //获得锁则对锁时长更新
                        try {
                            isLock = lock.tryLock(1, 10, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        //若延长锁失败，则变为从节点
                        if (!isLock) {
                            hasLock = false;
                            statusChange = true;
                        }
                    }
                }
                else {
                    boolean isLock;
                    try {
                        isLock = lock.tryLock(1, 10, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (isLock) {
                        //获得锁,变为主节点
                        hasLock = true;
                        statusChange = true;
                    }
                }
                //线程短暂休眠，避免redis锁竞争过于激烈
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private class ChangeStatus implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (!shutDown) {
                if (statusChange) {
                    if (hasLock) {
                        System.out.println("变成主节点");
                        //变成主节点
                        slaveNode.stop();
                        masterNode.start();
                    } else {
                        System.out.println("变成子节点");
                        //变成从节点
                        masterNode.stop();
                        slaveNode.start(queue, node);
                    }
                    statusChange = false;
                }
            }
        }
    }
}

