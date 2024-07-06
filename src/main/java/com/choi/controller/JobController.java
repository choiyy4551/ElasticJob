package com.choi.controller;

import com.choi.Enums.CodeEnum;
import com.choi.mapper.JobMapper;
import com.choi.pojo.JobInfo;
import com.choi.pojo.Result;
import com.choi.service.Node;
import com.choi.service.server.MasterNode;
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
    private Node node;
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private MasterNode masterNode;
    @RequestMapping("/addJob")
    public Result<Object> addJob(@RequestBody Map<String, String> map) {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setName(map.get("name"));
        jobInfo.setParam(map.get("param"));
        jobInfo.setScheduleType(map.get("scheduleType"));
        jobInfo.setScheduleParam(map.get("scheduleParam"));
        if (node.addJob(jobInfo)) {
            return Result.success(CodeEnum.ADD_JOB_SUCCESS);
        }
        return Result.failure(CodeEnum.ADD_JOB_ERR);
    }
    @RequestMapping("/getAllJobs")
    public Result<Object> getAllJobs() {
        System.out.println("");
        List<JobInfo> jobList = jobMapper.getAllJob();
        if (jobList.isEmpty()) {
            return Result.failure(null);
        }
        return Result.success(jobList);
    }
    @RequestMapping("/deleteJob")
    public Result<Object> deleteJob(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        if (masterNode.deleteJob(id)) {
            return Result.success(CodeEnum.DELETE_JOB_SUCCESS);
        }
        return Result.failure(CodeEnum.DELETE_JOB_ERR);
    }

}
