package com.choi.elastic_job.service;

import com.choi.elastic_job.pojo.ClientInfo;
import com.choi.elastic_job.pojo.JobInfo;
import com.choi.elastic_job.utils.JedisUtils;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import com.choi.elastic_job.proto.JobProto;
import com.choi.elastic_job.proto.ServiceGrpc;
@Service
@Slf4j
public class Client{
    @Autowired
    private JedisUtils jedisUtils;
    @Autowired
    private ClientInfo clientInfo;
    @Autowired
    private ClientStub clientStub;
    @Autowired
    private ClientJobHandler clientJobHandler;
    @Autowired
    private LeaderService service;
    ServiceGrpc.ServiceBlockingStub stub;

    private boolean shutdown;
    private boolean is_slave;
    public void start(){
        log.info("start as a slave");
        shutdown=false;
        is_slave=true;
        pullJobFromLeader pullJobFromLeader=new pullJobFromLeader();
        new Thread(pullJobFromLeader).start();
    }
    public void stop(){
        is_slave=false;
        log.info("stop as a slave");
    }

    private void getJob(){
        JobProto.ClientInfo request = JobProto.ClientInfo.newBuilder().setIoResources(clientInfo.getIo_resources()).setStorageResource(clientInfo.getStorage_resource()).setCpuSource(clientInfo.getCpu_resource()).build();
        JobProto.JobInfoReply reply;
        try{
            reply=stub.getJobs(request);
        }catch (StatusRuntimeException e){
            System.out.println("WARING, RPC failed: Status="+e.getStatus());
            throw e;
        }
        if(reply.getErrCode().getCode().equals("err")){
            log.error("get job failed, err:"+reply.getErrCode().getMessage());
        }else{
            String job_id=reply.getId();
            JobInfo jobInfo = service.select_job(job_id);
            clientJobHandler.runJob(jobInfo);
            sendJobResult(job_id);
        }
    }
    private void sendJobResult(String job_id){
        JobProto.SendJobResultRequest request=JobProto.SendJobResultRequest.newBuilder().setId(job_id).build();
        JobProto.JobResultResponse response;
        try{
            response = stub.sendJobResult(request);
        }catch (StatusRuntimeException e){
            System.out.println("WARING, RPC failed: Status="+e.getStatus());
            throw e;
        }
    }
    class pullJobFromLeader implements Runnable{
       @Override
       public void run() {
           while(!shutdown&&!is_slave) {
               String leader_ip = jedisUtils.get("leader");
               log.info("leader is" + leader_ip);
               stub=clientStub.getBlockingStub(leader_ip);
               if(shutdown){
                   log.info("localClient shutdown!");
                   break;
               }else {
                   getJob();
               }
           }
       }
    }
}
