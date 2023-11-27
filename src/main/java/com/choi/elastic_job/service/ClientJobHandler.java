package com.choi.elastic_job.service;

import com.choi.elastic_job.pojo.ClientInfo;
import com.choi.elastic_job.pojo.JobInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Service
public class ClientJobHandler{
    @Autowired
    LeaderService leaderService;
    @Autowired
    ClientInfo clientInfo;
    public void runJob(JobInfo jobInfo){
        onJobStart(jobInfo);
        //用Timer来模拟运行任务耗时
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            }
        },Integer.parseInt(jobInfo.getParam())*200);
        onJobEnd(jobInfo);
    }
    public void onJobStart(JobInfo jobInfo){
        if(jobInfo.getCategory().equals("io")){
            clientInfo.setIo_resources(Integer.parseInt(clientInfo.getIo_resources())-Integer.parseInt(jobInfo.getParam())+"");
        }else if(jobInfo.getCategory().equals("storage")){
            clientInfo.setStorage_resource(Integer.parseInt(clientInfo.getStorage_resource())-Integer.parseInt(jobInfo.getParam())+"");
        }else if(jobInfo.getCategory().equals("cpu")){
            clientInfo.setCpu_resource(Integer.parseInt(clientInfo.getCpu_resource())-Integer.parseInt(jobInfo.getParam())+"");
        }else {
            log.warn("Wrong in Job category");
        }
        log.info("Job:"+jobInfo.getId()+"start to run");
    }

    public void onJobEnd(JobInfo jobInfo){
        log.info("Job:"+jobInfo.getId()+"is done");
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("done success",jobInfo.getId());
        leaderService.insert_job_result(map);
    }
}
