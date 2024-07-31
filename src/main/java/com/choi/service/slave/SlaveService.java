package com.choi.service.slave;

import com.choi.pojo.JobInfo;

public interface SlaveService {
    /**
     * 用户可根据业务需求自主实现的runJob接口
     * @param jobInfo
     * @return
     */
    boolean runJob(JobInfo jobInfo);
}
