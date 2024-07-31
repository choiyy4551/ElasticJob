package com.choi.service.server;

import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import com.choi.Exception.MyException;

import java.util.List;

public interface MasterService {
    /**
     * 添加任务
     * @param jobInfo
     * @return
     */
    boolean addJob(JobInfo jobInfo);

    /**
     * 停用任务
     * @param name
     * @return
     * @throws MyException
     */
    boolean stopJob(String name) throws MyException;

    /**
     * 启用任务
     * @param name
     * @return
     * @throws MyException
     */
    boolean startJob(String name) throws MyException;

    /**
     * 删除任务
     * @param name
     * @return
     * @throws MyException
     */
    boolean deleteJob(String name) throws MyException;

    /**
     * 查询所有任务
     * @return
     */
    List<JobInfo> getAllJob();

    /**
     * 查询指定任务
     * @param name
     * @return
     */
    JobResult getJobResult(String name);

    /**
     * 查询所有任务结果
     * @return
     */
    List<JobResult> getAllJobResult();

    /**
     * 查询任务状态
     * @param name
     * @return
     */
    int getJobStatus(String name);
}
