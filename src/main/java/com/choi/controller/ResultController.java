package com.choi.controller;

import com.choi.Enums.CodeEnum;
import com.choi.mapper.JobMapper;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import com.choi.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/result")
public class ResultController {
    @Autowired
    private JobMapper jobMapper;
    @RequestMapping("/getAllResult")
    public Result<Object> getAllResult() {
        List<JobResult> jobResultList = jobMapper.getAllJobResult();
        if (jobResultList.isEmpty()) {
            return Result.failure(CodeEnum.DELETE_JOB_ERR);
        }
        return Result.success(jobResultList);
    }

    public Result<Object> getResultByName(String name) {
        JobInfo job = jobMapper.getJobByName(name);
        if (job == null) {
            return Result.failure(CodeEnum.DELETE_JOB_ERR);
        } else {
            JobResult jobResult = jobMapper.getJobResultById(job.getUuid());
            return Result.success(jobResult);
        }
    }
}
