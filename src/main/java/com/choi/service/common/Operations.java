package com.choi.service.common;

import com.choi.Exception.MyException;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;

public interface Operations {
    boolean addJob(JobInfo jobInfo, JobResult jobResult) throws MyException;
    boolean startJob(String name) throws MyException;
    boolean stopJob(String name) throws MyException;
    boolean deleteJob(String name) throws MyException;
    JobResult getJobResult(String uuid);
}
