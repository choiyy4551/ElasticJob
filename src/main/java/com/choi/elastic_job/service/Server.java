package com.choi.elastic_job.service;
import com.choi.elastic_job.mapper.LeaderService;
import com.choi.elastic_job.pojo.Configuration;
import com.choi.elastic_job.pojo.JobInfo;
import com.choi.elastic_job.proto.JobProto;
import com.choi.elastic_job.proto.ServiceGrpc;
import com.choi.elastic_job.proto.JobProto.JobResultResponse;
import com.choi.elastic_job.proto.JobProto.ErrorCode;
import com.choi.elastic_job.proto.JobProto.JobInfoReply;
import com.choi.elastic_job.utils.JedisUtils;
import com.choi.elastic_job.utils.JobUtil;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Server extends ServiceGrpc.ServiceImplBase {
    private Configuration configuration;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private JedisUtils jedisUtils;
    @Autowired
    private LeaderService leaderService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private Client client;
    private boolean own_lock;
    private boolean is_leader = false;
    private RLock lock;
    private io.grpc.Server server;
    private fetchLockThread fetchLockThread;
    private JobHandler jobHandler;


    public void modelChoose(Configuration configuration){
        this.configuration=configuration;
        lock=redissonClient.getLock(configuration.getClustername());
        //首次启动只会选择模式不会改变模式
        try {
            own_lock=lock.tryLock(1,10, TimeUnit.SECONDS);
            if(own_lock){
                leaderStart(configuration.getHostIp());
            }else{
                slaveStart();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //客户端或服务器端启动时就启动线程重复拉取集群锁
        fetchLockThread = new fetchLockThread();
        new Thread(fetchLockThread).start();
    }
    public void leaderStart(String ip)  {
        //所有人通信端口号默认4551，就不在redis中存入
        jedisUtils.set("leader",ip);
        is_leader=true;
        try {
            //发布服务
            server = ServerBuilder.forPort(configuration.getHostPort()).addService(new GrpcMethods()).build().start();
        } catch (Exception e) {
            log.warn("service error");
            e.printStackTrace();
        }
        //主节点启动任务管理线程，对任务进行排序和分配
        jobHandler = new JobHandler();
        jobHandler.setJudge(is_leader);
        new Thread(jobHandler).start();
        log.info("start as a leader, ip:"+ip);
    }
    public void leaderStop(){
        is_leader=false;
        jobHandler.setJudge(is_leader);
    }
    public void slaveStart(){
        client.start();
    }
    public void slaveStop(){
        client.stop();
    }
    public String addJob(JobInfo jobInfo){
        if(own_lock) {
            leaderService.addJob(jobInfo);
            return "success";
        }else{
            log.warn("You are not a leader!");
            return "error";
        }
    }
    private class GrpcMethods extends ServiceGrpc.ServiceImplBase{
        @Override
        public void getJobs(JobProto.ClientInfo request, StreamObserver<JobProto.JobInfoReply> responseObserver) {
            ErrorCode errorCode;
            JobInfoReply jobInfoReply;
            int io_index=0;
            int storage_index=0;
            int cpu_index=0;
            int ioResources =Integer.parseInt(request.getIoResources());
            int storageResource =Integer.parseInt(request.getStorageResource());
            int cpuReSource =Integer.parseInt(request.getCpuSource());
            String io_job=jobHandler.getIo_job(io_index);
            String storage_job=jobHandler.getStorage_job(storage_index);
            String cpu_job=jobHandler.getCpu_job(cpu_index);
            String io_id=jobUtil.getJobId(io_job);
            String io_param=jobUtil.getJobParam(io_job);
            String storage_id=jobUtil.getJobId(storage_job);
            String storage_param=jobUtil.getJobParam(storage_job);
            String cpu_id=jobUtil.getJobId(cpu_job);
            String cpu_param=jobUtil.getJobParam(cpu_job);
            if(Integer.parseInt(io_param)<ioResources){
                jobInfoReply=JobInfoReply.newBuilder().setId(io_id).build();
                responseObserver.onNext(jobInfoReply);
                responseObserver.onCompleted();
            }else if(Integer.parseInt(storage_param)>storageResource){
                jobInfoReply=JobInfoReply.newBuilder().setId(storage_id).build();
                responseObserver.onNext(jobInfoReply);
                responseObserver.onCompleted();
            }else if(Integer.parseInt(cpu_param)>cpuReSource){
                jobInfoReply=JobInfoReply.newBuilder().setId(cpu_id).build();
                responseObserver.onNext(jobInfoReply);
                responseObserver.onCompleted();
            }else{
                errorCode= ErrorCode.newBuilder().setCode("err").setMessage("dont have enough resource").build();
                jobInfoReply=JobInfoReply.newBuilder().setErrCode(errorCode).build();
                responseObserver.onNext(jobInfoReply);
                responseObserver.onCompleted();
            }
        }

        @Override
        public void sendJobResult(JobProto.SendJobResultRequest request, StreamObserver<JobProto.JobResultResponse> responseObserver) {
            JobResultResponse jobResultResponse;
            ErrorCode errorCode;
            String id=request.getId();
            String result = leaderService.select_job_result(Integer.parseInt(id));
            if(result.equals("done success")){
                leaderService.deleteJob(id);
                errorCode=ErrorCode.newBuilder().setCode("success").setMessage("job:"+id+"has done successfully").build();
                jobResultResponse=JobResultResponse.newBuilder().setErrCode(errorCode).build();
                responseObserver.onNext(jobResultResponse);
                responseObserver.onCompleted();
            }else{
                errorCode=ErrorCode.newBuilder().setCode("error").setMessage("job:"+id+"running wrong").build();
                jobResultResponse=JobResultResponse.newBuilder().setErrCode(errorCode).build();
                responseObserver.onNext(jobResultResponse);
                responseObserver.onCompleted();
            }
        }
    }
    //开启线程反复获取锁
    class fetchLockThread implements Runnable{
        @Override
        public void run() {
            while (true){
                try {
                    own_lock=lock.tryLock(1,10,TimeUnit.SECONDS);
                    if(own_lock){
                        String local_ip=configuration.getHostIp();
                        if(jedisUtils.get("leader").equals(local_ip)){
                            //leader中存储的ip和本地ip相同，说明重复获得锁
                            log.info("still as a leader!");
                        }else{
                            //不相同时说明leader改变，重新存入leader ip
                            log.info("leader change");
                            slaveStop();
                            leaderStart(local_ip);
                        }
                    }else if(is_leader){
                        log.info("leader has changed,start as a slave");
                        leaderStop();
                        slaveStart();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

