package com.choi.service.server;

import com.choi.grpc.*;
import com.choi.grpc.GrpcJobInfo;
import com.choi.mapper.JobMapper;
import com.choi.mapper.JobResultMapper;
import com.choi.pojo.*;
import com.choi.pojo.JobInfo;
import com.choi.Exception.MyException;
import com.choi.utils.MyUUID;
import com.choi.utils.ScheduleTime;
import com.choi.utils.StringAndInteger;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
@Data
public class MasterNode extends ElasticJobServiceGrpc.ElasticJobServiceImplBase implements MasterService {
    private Server server;
    private Configuration configuration;
    private boolean shutDown = true;
    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private MasterJobHandler masterJobHandler;
    @Autowired
    private MyUUID myUUID;
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private JobResultMapper jobResultMapper;
    @Autowired
    private ScheduleTime scheduleTime;
    private final int port = 4551;
    private final HashMap<String, NodeInfo> nodes = new HashMap<>();
    public void start() {
        try {
            System.out.println("我是领导!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            shutDown = false;
            //开启服务并向集群中写入ip:port信息
            server = ServerBuilder.forPort(port).addService(new GrpcMethods()).build().start();
            jedisCluster.set("host", configuration.getIp() + ":" + port);
            masterJobHandler.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void stop() {
        if (server != null) {
            server.shutdown();
            masterJobHandler.stop();
        }
        shutDown = true;
        System.out.println("主节点关闭");
    }
    @Override
    public boolean addJob(JobInfo jobInfo) {
        JobInfo job = jobMapper.getJobByName(jobInfo.getName());
        if (job != null) {
            //log
            return false;
        }
        jobInfo.setUuid(myUUID.createUUID());
        masterJobHandler.addJob(jobInfo);
        return true;
    }

    /**
     * 自定义异常用于主动抛出，引起spring事务rollback，保证同一任务在jobinfo和jobresult表中的一致性
     * @param name
     * @return
     * @throws MyException
     */
    @Transactional (rollbackFor = Exception.class)
    @Override
    public boolean stopJob(String name) throws MyException{
        JobInfo jobInfo = jobMapper.getJobByName(name);
        if (jobInfo == null)
            return false;
        if (jobMapper.stopJob(name) && jobResultMapper.stopJob(name))
            return true;
        throw new MyException("stopJob Error");
    }
    @Transactional (rollbackFor = Exception.class)
    @Override
    public boolean startJob(String name) throws MyException {
        JobInfo jobInfo = jobMapper.getJobByName(name);
        if (jobInfo == null)
            return false;
        if (jobMapper.startJob(name) && jobResultMapper.startJob(name))
            return true;
        throw new MyException("startJob Error");
    }
    @Override
    public List<JobInfo> getAllJob() {
        return jobMapper.getAllJob();
    }
    @Transactional (rollbackFor = Exception.class)
    @Override
    public boolean deleteJob(String name) {
        JobInfo job = jobMapper.getJobByName(name);
        if (job == null) {
            System.out.println("删除任务失败，不存在该任务!");
            return false;
        }
        jobMapper.deleteJob(name);
        jobResultMapper.deleteJob(name);
        return true;
    }
    @Override
    public List<JobResult> getAllJobResult() {
        return jobResultMapper.getAllJobResult();
    }
    @Override
    public JobResult getJobResult(String name) {
        JobResult jobResult = jobResultMapper.getJobResult(name);
        if (jobResult.getDeleteStatus() == 1) {
            return null;
        }
        return jobResult;
    }
    @Override
    public int getJobStatus(String name) {
        JobResult jobResult = jobResultMapper.getJobResult(name);
        if (jobResult.getDeleteStatus() == 1) {
            return -1;
        }
        return jobResult.getJobStatus();
    }

    @Override
    public JobResult getJobResultByName(String name) {
        JobResult jobResult = jobResultMapper.getJobResult(name);
        if (jobResult.getDeleteStatus() == 1) {
            return null;
        }
        return jobResult;
    }

    private List<JobInfo> divideJob(String resources, String maxParallel) {
        return masterJobHandler.divideJob(resources, maxParallel);
    }
    private class GrpcMethods extends ElasticJobServiceGrpc.ElasticJobServiceImplBase {
        @Override
        public void registerNode(RegisterNodeRequest request, StreamObserver<RegisterNodeReply> responseObserver) {
            ErrorCode errorCode;
            RegisterNodeReply registerNodeReply;
            //后续可以让slave节点转发消息
            if (shutDown) {
                errorCode = ErrorCode.newBuilder().setCode("MasterErr").setMessage("I am not Master").build();
                registerNodeReply = RegisterNodeReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(registerNodeReply);
                responseObserver.onCompleted();
                return;
            }
            String nodeId = request.getNodeId();
            String resources = request.getResources();
            String maxParallel = request.getMaxParallel();
            if (nodeId.isEmpty()) {
                errorCode = ErrorCode.newBuilder().setCode("NodeIdErr").setMessage("NodeId is Empty").build();
                registerNodeReply = RegisterNodeReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(registerNodeReply);
                responseObserver.onCompleted();
                return;
            }
            if (resources.isEmpty()) {
                errorCode = ErrorCode.newBuilder().setCode("ResourcesErr").setMessage("resources is Empty").build();
                registerNodeReply = RegisterNodeReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(registerNodeReply);
                responseObserver.onCompleted();
                return;
            }
            if (maxParallel.isEmpty()) {
                errorCode = ErrorCode.newBuilder().setCode("MaxParallelErr").setMessage("maxParallel is Empty").build();
                registerNodeReply = RegisterNodeReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(registerNodeReply);
                responseObserver.onCompleted();
                return;
            }
            if (!nodes.containsKey(nodeId)) {
                NodeInfo nodeInfo = new NodeInfo(nodeId, resources, Integer.parseInt(maxParallel));
                nodes.put(nodeId, nodeInfo);
                masterJobHandler.addRunningJobSize(StringAndInteger.StringToInteger(maxParallel));
                errorCode = ErrorCode.newBuilder().setCode("Success").setMessage("register successfully").build();
                registerNodeReply = RegisterNodeReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(registerNodeReply);
                responseObserver.onCompleted();
            } else {
                //已经注册，log
                errorCode = ErrorCode.newBuilder().setCode("Err").setMessage("already registered").build();
                registerNodeReply = RegisterNodeReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(registerNodeReply);
                responseObserver.onCompleted();
            }
        }

        @Override
        public void deregisterNode(DeregisterNodeRequest request, StreamObserver<DeregisterNodeReply> responseObserver) {
            ErrorCode errorCode;
            DeregisterNodeReply deregisterNodeReply;
            if (shutDown) {
                errorCode = ErrorCode.newBuilder().setCode("MasterErr").setMessage("I am not Master").build();
                deregisterNodeReply = DeregisterNodeReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(deregisterNodeReply);
                responseObserver.onCompleted();
                return;
            }
            String nodeId = request.getNodeId();
            if (nodeId.isEmpty()) {
                errorCode = ErrorCode.newBuilder().setCode("NodeIdErr").setMessage("NodeId is Empty").build();
                deregisterNodeReply = DeregisterNodeReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(deregisterNodeReply);
                responseObserver.onCompleted();
                return;
            }
            if (nodes.containsKey(nodeId)) {
                NodeInfo nodeInfo = nodes.get(nodeId);
                masterJobHandler.minusRunningJobSize(nodeInfo.getMaxParallel());
                nodes.remove(nodeId);
                errorCode = ErrorCode.newBuilder().setCode("Success").setMessage("success").build();
                deregisterNodeReply = DeregisterNodeReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(deregisterNodeReply);
                responseObserver.onCompleted();
            } else {
                errorCode = ErrorCode.newBuilder().setCode("Err").setMessage("have not registered").build();
                deregisterNodeReply = DeregisterNodeReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(deregisterNodeReply);
                responseObserver.onCompleted();
            }
        }

        @Override
        public void getJob(JobRequest request, StreamObserver<JobReply> responseObserver) {
            ErrorCode errorCode;
            JobReply jobReply;
            if (shutDown) {
                errorCode = ErrorCode.newBuilder().setCode("MasterErr").setMessage("I am not Master").build();
                jobReply = JobReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(jobReply);
                responseObserver.onCompleted();
                return;
            }
            String nodeId = request.getNodeId();
            String resource = request.getResource();
            String maxParallel = request.getParallelJobNum();
            if (nodeId.isEmpty()) {
                errorCode = ErrorCode.newBuilder().setCode("NodeIdErr").setMessage("nodeId is Empty").build();
                jobReply = JobReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(jobReply);
                responseObserver.onCompleted();
            } else {
                if (nodes.containsKey(nodeId)) {
                    if (resource.isEmpty()) {
                        errorCode = ErrorCode.newBuilder().setCode("ResourceErr").setMessage("resources is Empty").build();
                        jobReply = JobReply.newBuilder().setErr(errorCode).build();
                        responseObserver.onNext(jobReply);
                        responseObserver.onCompleted();
                        return;
                    }
                    if (maxParallel.isEmpty()) {
                        errorCode = ErrorCode.newBuilder().setCode("MaxParallelErr").setMessage("maxParallel is Empty").build();
                        jobReply = JobReply.newBuilder().setErr(errorCode).build();
                        responseObserver.onNext(jobReply);
                        responseObserver.onCompleted();
                        return;
                    }
                    //分配任务
                    //System.out.println("主节点分配任务");
                    List<JobInfo> jobInfoList = divideJob(resource, maxParallel);
                    if (jobInfoList.isEmpty()) {
                        errorCode = ErrorCode.newBuilder().setCode("Empty").setMessage("dont have any job").build();
                        jobReply = JobReply.newBuilder().setErr(errorCode).build();
                    } else {
                        errorCode = ErrorCode.newBuilder().setCode("Success").setMessage("success").build();
                        GrpcJobList.Builder grpcJobList = GrpcJobList.newBuilder();
                        for (JobInfo jobInfo : jobInfoList) {
                            com.choi.grpc.GrpcJobInfo grpcJobInfo = GrpcJobInfo.newBuilder().setId(jobInfo.getUuid()).setName(jobInfo.getName()).setParam(jobInfo.getParam()).build();
                            grpcJobList.addGrpcJobInfo(grpcJobInfo);
                        }
                        jobReply = JobReply.newBuilder().setGrpcJobList(grpcJobList).setErr(errorCode).build();
                    }
                } else {
                    errorCode = ErrorCode.newBuilder().setCode("Err").setMessage("have not registered").build();
                    jobReply = JobReply.newBuilder().setErr(errorCode).build();
                }
                responseObserver.onNext(jobReply);
                responseObserver.onCompleted();
            }
        }
        @Override
        public void addJob(AddJobRequest request, StreamObserver<AddJobReply> responseObserver) {
            ErrorCode errorCode;
            AddJobReply addJobReply;
            if (shutDown) {
                errorCode = ErrorCode.newBuilder().setCode("MasterErr").setMessage("I am not Master").build();
                addJobReply = AddJobReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(addJobReply);
                responseObserver.onCompleted();
                return;
            }
            String name = request.getName();
            String param = request.getParam();
            String scheduleType = request.getScheduleType();
            String scheduleParam = request.getScheduleParam();
            JobInfo jobInfo = new JobInfo();
            jobInfo.setName(name);
            jobInfo.setParam(param);
            jobInfo.setScheduleType(scheduleType);
            jobInfo.setScheduleParam(scheduleParam);
            if (MasterNode.this.addJob(jobInfo))
                errorCode = ErrorCode.newBuilder().setCode("Success").setMessage("addJob successfully").build();
            else
                errorCode = ErrorCode.newBuilder().setCode("Err").setMessage("addJob error").build();
            addJobReply = AddJobReply.newBuilder().setErr(errorCode).build();
            responseObserver.onNext(addJobReply);
            responseObserver.onCompleted();
        }
    }
}
