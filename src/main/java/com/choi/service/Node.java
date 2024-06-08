package com.choi.service;

import com.choi.config.ThreadPoolConfig;
import com.choi.pojo.Configuration;
import com.choi.pojo.JobInfo;
import com.choi.service.client.SlaveJobHandler;
import com.choi.service.client.SlaveNode;
import com.choi.service.server.MasterNode;
import com.choi.utils.MyUUID;
import com.choi.utils.RedisUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Data
@Service
public class Node {
    private boolean hasLock;
    private Configuration configuration;
    private boolean shutDown;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ThreadPoolConfig threadPoolConfig;
    @Autowired
    private MasterNode masterNode;
    @Autowired
    private SlaveNode slaveNode;
    @Autowired
    private SlaveJobHandler slaveJobHandler;
    @Autowired
    private MyUUID myUUID;
    private String nodeId;

    private final ThreadPoolTaskExecutor taskExecutor = threadPoolConfig.taskExecutor(); //可以添加final
    private final BlockingQueue<JobInfo> queue = new ArrayBlockingQueue<>(configuration.getMaxParallel());
    public void init(Configuration configuration) {
        this.configuration = configuration;
        nodeId = myUUID.createUUID();
        shutDown = false;
        //创建获取集群锁的线程
        taskExecutor.submit(new GetLock());
        masterNode.setConfiguration(configuration);
        slaveNode.setConfiguration(configuration);
    }
    public void stop() {
        shutDown = true;
        slaveNode.setSlaveShutdown(true);
    }
    public void statusChanged() {
        if (hasLock) {
            //变成主节点
            masterNode.start();
            slaveNode.stop();
        } else {
            //变成从节点
            masterNode.stop();
            slaveNode.start(queue);
        }
    }

    //获取集群锁线程
    private class GetLock implements Runnable {
        @Override
        public void run() {
            String clusterName = configuration.getClusterName();
            String host = configuration.getIp() + ":" + configuration.getPort();
            long expireMs = 10000;
            boolean jud;
            while (!shutDown) {
                if (hasLock) {
                    jud = redisUtils.setAndExpire(clusterName, 10000, host);
                    if (!jud) {
                        jud = redisUtils.setAndExpire(clusterName, 10000, host);
                        if (!jud) {
                            hasLock = false;
                            //主从节点转换
                            statusChanged();
                        }
                    }
                }
                else {
                    //可能有错
                    //集群锁还没被占有
                    if (redisUtils.get(clusterName) == null || redisUtils.get(clusterName).isEmpty()) {
                        jud = redisUtils.setAndExpire(clusterName, expireMs, host);
                        if (jud) {
                            hasLock = true;
                            //主从节点转换
                            statusChanged();
                        }
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
}

