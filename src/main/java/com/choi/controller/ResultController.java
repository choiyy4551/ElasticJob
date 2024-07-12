package com.choi.controller;

import com.choi.Enums.CodeEnum;
import com.choi.mapper.JobMapper;
import com.choi.mapper.JobResultMapper;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import com.choi.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Result")
public class ResultController {
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private JobResultMapper jobResultMapper;
    @RequestMapping("/getAllResult")
    public Result<Object> getAllResult() {
        List<JobResult> jobResultList = jobResultMapper.getAllJobResult();
        if (jobResultList.isEmpty()) {
            return Result.failure(CodeEnum.DELETE_JOB_ERR);
        }
        return Result.success(jobResultList);
    }

    @RequestMapping("/getResultByName")
    public Result<Object> getResultByName(String name) {
        JobInfo job = jobMapper.getJobByName(name);
        if (job == null) {
            return Result.failure(CodeEnum.DELETE_JOB_ERR);
        } else {
            JobResult jobResult = jobResultMapper.getJobResultById(job.getUuid());
            return Result.success(jobResult);
        }
    }
    @RequestMapping("/getJobStatus")
    public Result<Object> getJobStatus(String name) {
        JobInfo job = jobMapper.getJobByName(name);
        if (job == null) {
            return Result.failure(CodeEnum.DONT_HAVE_SUCH_JOB);
        }
        int jobStatus = jobResultMapper.getJobStatusByName(name);
        return Result.success(jobStatus);
    }
}
