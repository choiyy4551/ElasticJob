package com.choi.service.server;

import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import com.choi.Exception.MyException;

import java.util.List;

public interface MasterService {
    boolean addJob(JobInfo jobInfo);
    boolean stopJob(String name) throws MyException;
    boolean startJob(String name) throws MyException;
    boolean deleteJob(String name);
    List<JobInfo> getAllJob();
    JobResult getJobResult(String name);
    List<JobResult> getAllJobResult();
    int getJobStatus(String name);
}
