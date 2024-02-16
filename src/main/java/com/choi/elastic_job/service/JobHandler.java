package com.choi.elastic_job.service;

import com.choi.elastic_job.mapper.LeaderService;
import com.choi.elastic_job.pojo.JobCompareParam;
import com.choi.elastic_job.utils.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobHandler implements Runnable{
    @Autowired
    private LeaderService leaderService;
    private boolean judge;
    private int Resource;
    public void setJudge(boolean judge) {
        this.judge = judge;
    }

    List<JobCompareParam>  jobs;
    //反复拉取数据库中的数据
    @Override
    public void run() {
        while (judge) {
            jobs = leaderService.select_jobs();
            Collections.sort(jobs);
        }
    }
    public boolean judgeResource(int Resource) {
        this.Resource = Resource;
        if(Resource < ConvertUtil.toInt(jobs.get(0).getParam()))
            return false;
        return true;
    }
    public List<String> getIoJobs(){
        List<String> jobIds = new ArrayList<>();
        for (JobCompareParam param: jobs) {
            int p = ConvertUtil.toInt(param.getParam());
            if(Resource - p > 0) {
                jobIds.add(param.getId());
                Resource -= p;
            }
        }
        return jobIds;
    }
}
