package com.choi.elastic_job.service;

import com.choi.elastic_job.mapper.LeaderService;
import com.choi.elastic_job.pojo.ClientInfo;
import com.choi.elastic_job.pojo.JobInfo;
import com.choi.elastic_job.utils.ConvertUtil;
import com.choi.elastic_job.utils.JedisUtils;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.choi.elastic_job.proto.JobProto;
import com.choi.elastic_job.proto.ServiceGrpc;

import java.util.ArrayList;
import java.util.Optional;

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
        pullJobFromLeader pullJobFromLeader = new pullJobFromLeader();
        new Thread(pullJobFromLeader).start();
    }
    public void stop(){
        is_slave=false;
        log.info("stop as a slave");
    }

    public void getJob() {
        //每次通信前刷新通道
        try {
            stub = clientStub.getBlockingStub(jedisUtils.get("leader"));
        }catch (Exception e) {
            e.printStackTrace();
        }
        JobProto.ClientInfo request = JobProto.ClientInfo.newBuilder().setResources(clientInfo.getIo_resources()).build();
        JobProto.JobList reply;
        try {
            reply = stub.getJobs(request);
        } catch (StatusRuntimeException e) {
            System.out.println("WARING, RPC failed: Status="+e.getStatus());
            throw e;
        }
        //获取任务失败，自身资源数不够
        if("error".equals(reply.getErrCode().getCode())) {
            log.error("get job failed, dont have enough resource");
            return;
        }
        ArrayList<String> list = ConvertUtil.protocolStringListToList(reply.getArrayList());
        clientJobHandler.transmitJob(list);
    }
    public void sendJobResult(String job_id){
        JobProto.SendJobResultRequest request = JobProto.SendJobResultRequest.newBuilder().setId(job_id).build();
        JobProto.JobResultResponse response;
        try{
            response = stub.sendJobResult(request);
        }catch (StatusRuntimeException e){
            System.out.println("WARING, RPC failed: Status="+e.getStatus());
            throw e;
        }
        if("error".equals(Optional.of(response.getErrCode()).map(JobProto.ErrorCode::getCode).orElse(""))){
            //没发送成功ping服务器看看连接是否正常
            JobProto.ErrorCode errorCode = JobProto.ErrorCode.newBuilder().setCode("ping").setMessage("confirm connect").build();
            JobProto.ErrorCode reply = stub.ping(errorCode);
            if("error".equals(reply.getCode())){
                //连接异常，尝试重新连接服务器
                 jedisUtils.get("leader");

            }else{
                //连接正常，重新发送任务结果
                try {
                    stub.sendJobResult(request);
                }catch (Exception e){
                    System.out.println();
                }
            }
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
