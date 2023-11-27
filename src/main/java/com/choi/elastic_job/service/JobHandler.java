package com.choi.elastic_job.service;

import com.choi.elastic_job.pojo.JobCompareParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobHandler implements Runnable{
    @Autowired
    private LeaderService leaderService;
    private boolean judge;

    public void setJudge(boolean judge) {
        this.judge = judge;
    }

    List<JobCompareParam>  io_jobs;
    List<JobCompareParam> storage_jobs;
    List<JobCompareParam> cpu_jobs;
    //反复拉取数据库中的数据
    @Override
    public void run() {
        while (judge) {
            io_jobs = leaderService.select_io_jobs();
            storage_jobs = leaderService.select_storage_jobs();
            cpu_jobs = leaderService.select_cpu_jobs();
        }
    }
    public String getIo_job(int index){
        Collections.sort(io_jobs);
        return io_jobs.get(index).getId()+":"+io_jobs.get(index).getParam();
    }
    public String getStorage_job(int index){
        Collections.sort(storage_jobs);
        return storage_jobs.get(index).getId()+":"+storage_jobs.get(index).getParam();
    }
    public String getCpu_job(int index){
        Collections.sort(cpu_jobs);
        return cpu_jobs.get(index).getId()+":"+cpu_jobs.get(index).getParam();
    }
}
