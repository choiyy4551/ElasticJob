package com.choi.controller;

import com.choi.Enums.CodeEnum;
import com.choi.pojo.JobInfo;
import com.choi.pojo.Result;
import com.choi.service.master.MasterNode;
import com.choi.Exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Job")
public class JobController {
    @Autowired
    private MasterNode masterNode;
    @RequestMapping("/addJob")
    public Result<Object> addJob(@RequestBody Map<String, String> map) {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setName(map.get("name"));
        jobInfo.setParam(map.get("param"));
        jobInfo.setScheduleType(map.get("scheduleType"));
        jobInfo.setScheduleParam(map.get("scheduleParam"));
        if (masterNode.addJob(jobInfo)) {
            return Result.success(CodeEnum.ADD_JOB_SUCCESS);
        }
        return Result.failure(CodeEnum.ADD_JOB_ERR);
    }
    @RequestMapping("/getAllJobs")
    public Result<Object> getAllJobs() {
        List<JobInfo> jobList = masterNode.getAllJob();
        if (jobList.isEmpty()) {
            return Result.failure(null);
        }
        return Result.success(jobList);
    }
    @RequestMapping("/deleteJob")
    public Result<Object> deleteJob(@RequestBody Map<String, String> map) {
        String name = map.get("name");
        try {
            if (masterNode.deleteJob(name)) {
                return Result.success(CodeEnum.DELETE_JOB_SUCCESS);
            }
        } catch (MyException e) {
            e.printStackTrace();
        }
        return Result.failure(CodeEnum.DELETE_JOB_ERR);
    }
    @RequestMapping("/startJob")
    public Result<Object> startJob(@RequestBody Map<String, String> map) {
        String name = map.get("name");
        try {
            if (masterNode.startJob(name)) {
                return Result.success(CodeEnum.SUCCESS);
            }
        } catch (MyException e) {
            e.printStackTrace();
        }
        return Result.failure(CodeEnum.ERR);
    }
    @RequestMapping("/stopJob")
    public Result<Object> stopJob(@RequestBody Map<String, String> map) {
        String name = map.get("name");
        try {
            if (masterNode.stopJob(name)) {
                return Result.success(CodeEnum.SUCCESS);
            }
        } catch (MyException e) {
            e.printStackTrace();
        }
        return Result.failure(CodeEnum.ERR);
    }
}
