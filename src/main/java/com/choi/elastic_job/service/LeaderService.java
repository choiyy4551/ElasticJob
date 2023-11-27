package com.choi.elastic_job.service;


import com.choi.elastic_job.pojo.JobCompareParam;
import com.choi.elastic_job.pojo.JobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Mapper
@Repository
public interface LeaderService {
    void addJob(JobInfo jobInfo);
    void insert_job_result(Map<String,Object> map);
    JobInfo select_job(String id);
    List<JobCompareParam> select_io_jobs();
    List<JobCompareParam> select_storage_jobs();
    List<JobCompareParam> select_cpu_jobs();
    String select_job_result(int id);
    void deleteJob(String id);
}
