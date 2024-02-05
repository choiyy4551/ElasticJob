package com.choi.elastic_job.controller;

import com.choi.elastic_job.pojo.JobInfo;
import com.choi.elastic_job.mapper.LeaderService;
import com.choi.elastic_job.service.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequestMapping("/Job")
@RestController
public class JobMapper {
        @Autowired
        private Server server;
        @Autowired
        private LeaderService leaderService;
        @RequestMapping("/AddJob")
        public String AddJob(@RequestBody HashMap<String,String> map){
            JobInfo jobInfo=new JobInfo();
            jobInfo.setId(map.get("id"));
            jobInfo.setName(map.get("name"));
            jobInfo.setCategory(map.get("category"));
            jobInfo.setParam(map.get("param"));
            jobInfo.setScheduleType(map.get("scheduleType"));
            jobInfo.setScheduleParam(map.get("scheduleParam"));
            jobInfo.setPriority(Integer.parseInt(map.get("priority")));
            return server.addJob(jobInfo);
        }
        @RequestMapping("/GetAllJob")
        public void getAllJob(){
            leaderService.GetAllJob();
        }
        @RequestMapping("/GetJobById")
        public void GetJobById(@RequestBody String id){
            leaderService.select_job(id);
        }
}
