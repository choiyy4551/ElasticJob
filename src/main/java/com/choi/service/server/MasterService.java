package com.choi.service.server;

import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;

import java.util.List;

public interface MasterService {
    boolean addJob(JobInfo jobInfo);
    JobResult getJobResult(String uuid);
    boolean deleteJob(String uuid);
    public List<JobResult> getAllJobResult();
    public List<JobInfo> getAllJob();
}
