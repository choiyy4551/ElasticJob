package com.choi.controller;

import com.choi.Enums.CodeEnum;
import com.choi.pojo.JobResult;
import com.choi.pojo.Result;
import com.choi.service.server.MasterNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Result")
public class ResultController {
    @Autowired
    MasterNode masterNode;
    @RequestMapping("/getAllResult")
    public Result<Object> getAllResult() {
        List<JobResult> jobResultList = masterNode.getAllJobResult();
        if (jobResultList.isEmpty()) {
            return Result.failure(CodeEnum.DELETE_JOB_ERR);
        }
        return Result.success(jobResultList);
    }
    @RequestMapping("/getJobResult")
    public Result<Object> getJobResult(@RequestBody Map<String, String> map) {
        String name = map.get("name");
        JobResult jobResult = masterNode.getJobResult(name);
        if (jobResult == null) {
            return Result.failure(CodeEnum.ERR);
        }
        return Result.success(jobResult);
    }
    @RequestMapping("/getJobStatus")
    public Result<Object> getJobStatus(@RequestBody Map<String, String> map) {
        String name = map.get("name");
        int jobStatus = masterNode.getJobStatus(name);
        if (jobStatus == -1) {
            return Result.failure(CodeEnum.ERR);
        }
        return Result.success(CodeEnum.SUCCESS);
    }
}
