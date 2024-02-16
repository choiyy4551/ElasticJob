package com.choi.elastic_job.service;

import com.choi.elastic_job.mapper.LeaderService;
import com.choi.elastic_job.pojo.ClientInfo;
import com.choi.elastic_job.pojo.JobInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ClientJobHandler{
    @Autowired
    LeaderService leaderService;
    @Autowired
    ClientInfo clientInfo;
    @Autowired
    Client client;

    public void runJob(JobInfo jobInfo){
        onJobStart(jobInfo);
        jobInfo.getClientInterface().run();
        onJobEnd(jobInfo);
    }
    public void onJobStart(JobInfo jobInfo){
        log.info("Job:"+jobInfo.getId()+"start to run");
    }

    public void onJobEnd(JobInfo jobInfo){
        log.info("Job:"+jobInfo.getId()+"is done");
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("done success",jobInfo.getId());
        leaderService.insert_job_result(map);
        client.sendJobResult(jobInfo.getId());
    }
    public void transmitJob(ArrayList<String> arr){
        //获取jobInfo执行任务
        for (String jobId: arr) {
            JobInfo jobInfo = leaderService.select_job(jobId);
            runJob(jobInfo);
        }
    }
}
