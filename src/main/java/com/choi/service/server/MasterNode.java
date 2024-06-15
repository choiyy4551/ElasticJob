package com.choi.service.server;

import com.choi.config.ThreadPoolConfig;
import com.choi.grpc.*;
import com.choi.mapper.JobMapper;
import com.choi.pojo.Configuration;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import com.choi.pojo.NodeInfo;
import com.choi.service.Node;
import com.choi.utils.MyUUID;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
@Data
public class MasterNode extends ElasticJobServiceGrpc.ElasticJobServiceImplBase implements MasterService{
    private Server server;
    private final int port = 4551;
    private Configuration configuration;
    @Autowired
    ThreadPoolConfig threadPoolConfig;
    private final ThreadPoolTaskExecutor taskExecutor = threadPoolConfig.taskExecutor();
    @Autowired
    private MasterJobHandler masterJobHandler;
    @Autowired
    private MyUUID myUUID;
    @Autowired
    private Node node;
    @Autowired
    private JobMapper jobMapper;
    private final HashMap<String, NodeInfo> nodes = new HashMap<>();
    public void start() {
        try {
            server = ServerBuilder.forPort(port).addService(new GrpcMethods()).build().start();
            //开启线程对任务队列分配
            taskExecutor.submit(masterJobHandler.new changeJob());
            masterJobHandler.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    @Override
    public JobResult getJobResult(String uuid) {
        JobInfo job = jobMapper.getJobById(uuid);
        if (job.getDeleteStatus() == 1) {
            //log 任务已经删除
            return new JobResult();
        }
        return jobMapper.getJobResultById(uuid);
    }
    @Override
    public List<JobResult> getAllJobResult() {
        return jobMapper.getAllJobResult();
    }
    @Override
    public List<JobInfo> getAllJob() {
        return jobMapper.getAllJob();
    }
    @Override
    public boolean deleteJob(String uuid) {
        JobInfo job = jobMapper.getJobById(uuid);
        if (job == null) {
            //log
            return false;
        }
        return jobMapper.deleteJob(uuid);
    }
    private List<JobInfo> divideJob(String resources, String maxParallel) {
        return masterJobHandler.divideJob(resources, maxParallel);
    }
    private class GrpcMethods extends ElasticJobServiceGrpc.ElasticJobServiceImplBase {
        @Override
        public void registerNode(RegisterNodeRequest request, StreamObserver<RegisterNodeReply> responseObserver) {
            ErrorCode errorCode;
            RegisterNodeReply registerNodeReply;
            String nodeId = request.getNodeId();
            String resources = request.getResources();
            String maxParallel = request.getMaxParallel();
            //后续可以让slave节点转发消息
            if (!node.isHasLock()) {
                errorCode = ErrorCode.newBuilder().setCode("MasterErr").setMessage("I am not Master").build();
                registerNodeReply = RegisterNodeReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(registerNodeReply);
                responseObserver.onCompleted();
                return;
            }
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
            if (registerJudge(nodeId)) {
                NodeInfo nodeInfo = new NodeInfo(nodeId, resources, Integer.parseInt(maxParallel));
                nodes.put(nodeId, nodeInfo);
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
            if (!node.isHasLock()) {
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
            String nodeId = request.getNodeId();
            String resource = request.getResource();
            String maxParallel = request.getParallelJobNum();
            if (!node.isHasLock()) {
                errorCode = ErrorCode.newBuilder().setCode("MasterErr").setMessage("I am not Master").build();
                jobReply = JobReply.newBuilder().setErr(errorCode).build();
                responseObserver.onNext(jobReply);
                responseObserver.onCompleted();
                return;
            }
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
                    List<JobInfo> jobInfoList = divideJob(resource, maxParallel);
                    errorCode = ErrorCode.newBuilder().setCode("Success").setMessage("success").build();
                    JobList.Builder jobListBuilder = JobList.newBuilder();
                    jobListBuilder.addAllJobInfo(jobInfoList);
                    jobReply = JobReply.newBuilder().setJobList(jobListBuilder).setErr(errorCode).build();
                } else {
                    errorCode = ErrorCode.newBuilder().setCode("Err").setMessage("have not registered").build();
                    jobReply = JobReply.newBuilder().setErr(errorCode).build();
                }
                responseObserver.onNext(jobReply);
                responseObserver.onCompleted();
            }
        }
        @Override
        public void addJob(AddJobInfo request, StreamObserver<AddJobReply> responseObserver) {
            ErrorCode errorCode;
            AddJobReply addJobReply;
            if (!node.isHasLock()) {
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

    private boolean registerJudge(String nodeId) {
        return !nodes.containsKey(nodeId);
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
            masterJobHandler.stop();
        }
    }
}
