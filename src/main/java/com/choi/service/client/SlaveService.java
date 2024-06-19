package com.choi.service.client;

import com.choi.pojo.JobInfo;

public interface SlaveService {
    boolean runJob(JobInfo jobInfo);
}
