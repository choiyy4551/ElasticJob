package com.choi.service.common;

import com.choi.Exception.MyException;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;

public interface Operations {
    /**
     * 持久化JobInfo表和JobResult表，确保同一任务在两表中一致
     * @param jobInfo
     * @param jobResult
     * @return
     * @throws MyException
     */
    boolean addJob(JobInfo jobInfo, JobResult jobResult) throws MyException;

    /**
     * JobInfo和JobResult中的任务一致启动
     * @param name
     * @return
     * @throws MyException
     */
    boolean startJob(String name) throws MyException;

    /**
     * JobInfo和JobResult中的任务一致停用
     * @param name
     * @return
     * @throws MyException
     */
    boolean stopJob(String name) throws MyException;

    /**
     * JobInfo和JobResult中的任务一致删除
     * @param name
     * @return
     * @throws MyException
     */
    boolean deleteJob(String name) throws MyException;

    /**
     * 获取JobResult
     * @param uuid
     * @return
     */
    JobResult getJobResult(String uuid);
}
