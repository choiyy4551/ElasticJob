package com.choi.service.client;

import com.choi.config.ThreadPoolConfig;
import com.choi.grpc.*;
import com.choi.mapper.JobMapper;
import com.choi.pojo.Configuration;
import com.choi.pojo.JobInfo;
import com.choi.service.Node;
import com.choi.utils.MyUUID;
import com.choi.utils.RedisUtils;
import com.choi.utils.StringAndInteger;
import io.grpc.StatusRuntimeException;
import lombok.Data;
import java.lang.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Service
@Data
public class SlaveNode {
    private Configuration configuration;
    @Autowired
    private MyUUID myUUID;
    @Autowired
    private SlaveStub slaveStub;
    @Autowired
    private Node node;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    ThreadPoolConfig threadPoolConfig;
    @Autowired
    SlaveJobHandler slaveJobHandler;
    private boolean slaveShutdown;
    private final ThreadPoolTaskExecutor taskExecutor = threadPoolConfig.taskExecutor();
    private static final Object object = new Object();
    private ElasticJobServiceGrpc.ElasticJobServiceBlockingStub stub;
    private String nodeId = node.getNodeId();
    private String clusterName;
    public void start(BlockingQueue<JobInfo> queue) {
        slaveShutdown = false;
        Producer producer = new Producer(queue);
        taskExecutor.submit(producer);
        slaveJobHandler.start(queue);
    }

    public void stop() {
        slaveShutdown = true;
    }

    public List<JobInfo> getJobFromMaster() {
        clusterName = node.getConfiguration().getClusterName();
        String host = redisUtils.get(clusterName);
        List<JobInfo> jobInfoList = null;
        //类似双重校验锁
        if (!slaveShutdown) {
            //host为空，可能代表系统启动后又关闭了
            if (host == null || host.isEmpty()) {
                synchronized (object) {
                    try {
                        object.wait(5000);
                        if (slaveShutdown) {
                            //节点关闭
                            //return ;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            stub = slaveStub.getBlockingStub(host);
            //没有注册且节点未关闭则反复注册
            while (!register()) {
                synchronized (object) {
                    try {
                        object.wait(5000);
                        if (slaveShutdown) {
                            //节点关闭
                            //return;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            jobInfoList = getJob();
            return jobInfoList;
        }
        return jobInfoList;
    }
    private boolean register() {
        String resource = node.getConfiguration().getResources();
        String maxParallel = node.getConfiguration().getResources();
        RegisterNodeRequest registerNodeRequest = RegisterNodeRequest.newBuilder().setNodeId(nodeId)
                .setResources(resource).setMaxParallel(maxParallel).build();
        RegisterNodeReply registerNodeReply;
        try {
            registerNodeReply = stub.registerNode(registerNodeRequest);
        } catch (StatusRuntimeException e) {
            throw e;
        }
        String code = registerNodeReply.getErr().getCode();
        if ("MasterErr".equals(code)) {
            //log
            synchronized (object) {
                try {
                    object.wait(5000);
                    if (slaveShutdown) {
                        //节点关闭
                        return false;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            String host = redisUtils.get(clusterName);
            stub = slaveStub.getBlockingStub(host);
            return register();
        }
        if ("NodeIdErr".equals(code)) {
            //
            return false;
        }
        if ("ResourceErr".equals(code)) {
            //
            return false;
        }
        if ("MaxParallelErr".equals(code)) {
            //
            return false;
        }
        if ("Success".equals(code)) {
            //
            return true;
        }
        if ("Err".equals(code)) {
            //
            return false;
        }
        return false;
    }
    private boolean deregister() {
        DeregisterNodeRequest deregisterNodeRequest = DeregisterNodeRequest.newBuilder().setNodeId(nodeId).build();
        DeregisterNodeReply deregisterNodeReply;
        try {
            deregisterNodeReply = stub.deregisterNode(deregisterNodeRequest);
        } catch (StatusRuntimeException e) {
            throw e;
        }
        String code = deregisterNodeReply.getErr().getCode();
        if ("MasterErr".equals(code)) {
            //
            synchronized (object) {
                try {
                    object.wait(5000);
                    if (slaveShutdown) {
                        //节点关闭
                        stop();
                        return false;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            String host = redisUtils.get(clusterName);
            stub = slaveStub.getBlockingStub(host);
            return deregister();
        }
        if ("NodeIdErr".equals(code)) {
            //
            return false;
        }
        if ("Success".equals(code)) {
            //
            return true;
        }
        return false;
    }

    private List<JobInfo> getJob() {
        JobRequest jobRequest = JobRequest.newBuilder().setNodeId(nodeId).setResource(node.getConfiguration().getResources())
                .setParallelJobNum(StringAndInteger.IntegerToString(node.getConfiguration().getMaxParallel())).build();
        JobReply jobReply;
        List<JobInfo> jobInfos = new ArrayList<>();
        try {
            jobReply = stub.getJob(jobRequest);
        } catch (RuntimeException e) {
            throw e;
        }
        String code = jobReply.getErr().getCode();
        if ("MasterErr".equals(code)) {
            synchronized (object) {
                try {
                    object.wait(5000);
                    if (slaveShutdown) {
                        //节点关闭
                        stop();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            String host = redisUtils.get(clusterName);
            stub = slaveStub.getBlockingStub(host);
            return getJob();
        }
        if ("NodeIdErr".equals(code)) {
            //log
        }
        if ("ResourceErr".equals(code)) {
            //
        }
        if ("MaxParallelErr".equals(code)) {
            //
        }
        if ("success".equals(code)) {
            for (com.choi.grpc.JobInfo jobInfo : jobReply.getJobList().getJobInfoList()) {
                String uuid = jobInfo.getId();
                JobInfo jobById = jobMapper.getJobById(uuid);
                jobInfos.add(jobById);
            }
        }
        return jobInfos;
    }
    class Producer implements Runnable {
        private final BlockingQueue<JobInfo> queue;
        public Producer(BlockingQueue<JobInfo> queue) {
            this.queue = queue;
        }
        @Override
        public void run() {
            if (slaveShutdown)
                return;
            try {
                //生产者模型，阻塞队列会自动唤醒消费者
                List<JobInfo> jobInfos = getJobFromMaster();
                if (jobInfos == null || jobInfos.isEmpty())
                    return;
                for (JobInfo jobInfo : jobInfos) {
                    queue.put(jobInfo);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}