package com.choi.service.client;

import com.choi.grpc.*;
import com.choi.mapper.JobMapper;
import com.choi.pojo.Configuration;
import com.choi.pojo.JobInfo;
import com.choi.service.Node;
import com.choi.utils.MyUUID;
import com.choi.utils.StringAndInteger;
import io.grpc.StatusRuntimeException;
import lombok.Data;
import java.lang.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

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
    private JobMapper jobMapper;
    @Autowired
    private JedisCluster jedisCluster;
    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    SlaveJobHandler slaveJobHandler;
    private boolean shutDown;
    private boolean registered = false;
    private Node node;
    private static final Object object = new Object();
    private ElasticJobServiceGrpc.ElasticJobServiceBlockingStub stub = null;
    private String clusterName;
    //用来存储上一个主节点host，判断主节点是否发生改变
    private String host;
    public void start(BlockingQueue<JobInfo> queue, Node node) {
        System.out.println("我是打工人！！！！！！！！！！！！！！！！！！！！！！！！！！");
        shutDown = false;
        this.node = node;
        register();
        slaveJobHandler.start(queue, node.getNodeId());
        Producer producer = new Producer(queue);
        taskExecutor.submit(producer);
    }

    public void stop() {
        if (stub != null) {
            slaveStub.shutDown();
        }
        if (registered) {
            deregister();
        }
        shutDown = true;
        slaveJobHandler.stop();
        System.out.println("子节点结束");
    }

    public List<JobInfo> getJobFromMaster() {
        String host = jedisCluster.get("host");
        List<JobInfo> jobInfoList;
        //类似双重校验锁
        if (!shutDown) {
            //host为空，可能代表系统启动后又关闭了
            if (host == null || host.isEmpty()) {
                synchronized (object) {
                    try {
                        object.wait(5000);
                        if (shutDown) {
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
                        if (shutDown) {
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
        return null;
    }
    private boolean register() {
        String resource = node.getConfiguration().getResources();
        String maxParallel = node.getConfiguration().getResources();
        RegisterNodeRequest registerNodeRequest = RegisterNodeRequest.newBuilder().setNodeId(node.getNodeId())
                .setResources(resource).setMaxParallel(maxParallel).build();
        RegisterNodeReply registerNodeReply;
        String host = jedisCluster.get("host");
        if (host.equals(this.host))
            return true;
        this.host = host;
        stub = slaveStub.getBlockingStub(host);
        try {
            registerNodeReply = stub.registerNode(registerNodeRequest);
        } catch (StatusRuntimeException e) {
            throw e;
        }
        String code = registerNodeReply.getErr().getCode();
        System.out.println(code);
        if ("MasterErr".equals(code)) {
            //log
            synchronized (object) {
                try {
                    object.wait(5000);
                    if (shutDown) {
                        //节点关闭
                        return false;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
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
            System.out.println("注册成功");
            registered = true;
            return true;
        }
        if ("Err".equals(code)) {
            //
            return false;
        }
        return false;
    }
    private boolean deregister() {
        if (slaveStub.isShutDown()) {
            return true;
        }
        DeregisterNodeRequest deregisterNodeRequest = DeregisterNodeRequest.newBuilder().setNodeId(node.getNodeId()).build();
        DeregisterNodeReply deregisterNodeReply;
        String host = jedisCluster.get("host");
        stub = slaveStub.getBlockingStub(host);
        System.out.println("11");
        try {
            deregisterNodeReply = stub.deregisterNode(deregisterNodeRequest);
        } catch (StatusRuntimeException e) {
            throw e;
        }
        System.out.println("33");
        String code = deregisterNodeReply.getErr().getCode();
        if ("MasterErr".equals(code)) {
            //
            synchronized (object) {
                try {
                    object.wait(5000);
                    if (shutDown) {
                        //节点关闭
                        stop();
                        return false;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return register();
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
        JobRequest jobRequest = JobRequest.newBuilder().setNodeId(node.getNodeId()).setResource(node.getConfiguration().getResources())
                .setParallelJobNum(StringAndInteger.IntegerToString(node.getConfiguration().getMaxParallel())).build();
        JobReply jobReply;
        List<JobInfo> jobInfoList = new ArrayList<>();
        String host = jedisCluster.get("host");
        stub = slaveStub.getBlockingStub(host);
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
                    if (shutDown) {
                        //节点关闭
                        stop();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
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
        if ("Success".equals(code)) {
            System.out.println("子节点获取到任务");
            List<GrpcJobInfo> grpcJobInfoList = jobReply.getGrpcJobList().getGrpcJobInfoList();
            for (GrpcJobInfo grpcJobInfo : grpcJobInfoList) {
                String uuid = grpcJobInfo.getId();
                JobInfo jobById = jobMapper.getJobById(uuid);
                jobInfoList.add(jobById);
                System.out.println(jobById);
            }
        }
        return jobInfoList;
    }
    public boolean addJob(JobInfo jobInfo) {
        AddJobRequest addJobRequest = AddJobRequest.newBuilder().setName(jobInfo.getName()).setParam(jobInfo.getParam())
                .setScheduleType(jobInfo.getScheduleType()).setScheduleParam(jobInfo.getScheduleParam()).build();
        AddJobReply addJobReply;
        String host = jedisCluster.get("host");
        stub = slaveStub.getBlockingStub(host);
        try {
            addJobReply = stub.addJob(addJobRequest);
        } catch (RuntimeException e) {
            throw e;
        }
        String code = addJobReply.getErr().getCode();
        if ("MasterErr".equals(code)) {
            synchronized (object) {
                try {
                    object.wait(5000);
                    if (shutDown) {
                        //节点关闭
                        stop();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return addJob(jobInfo);
        }
        if ("Success".equals(code)) {
            return true;
        }
        return false;
    }
    class Producer implements Runnable {
        private final BlockingQueue<JobInfo> queue;
        public Producer(BlockingQueue<JobInfo> queue) {
            this.queue = queue;
        }
        @Override
        public void run() {
            while (!shutDown) {
                try {
                    //生产者模型，阻塞队列会自动唤醒消费者
                    List<JobInfo> jobInfoList = getJobFromMaster();
                    if (jobInfoList == null || jobInfoList.isEmpty())
                        return;
                    for (JobInfo jobInfo : jobInfoList) {
                        queue.put(jobInfo);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
