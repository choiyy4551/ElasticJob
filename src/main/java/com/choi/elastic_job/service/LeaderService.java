package com.choi.elastic_job.service;


import com.choi.elastic_job.pojo.JobCompareParam;
import com.choi.elastic_job.pojo.JobInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Mapper
@Repository
public interface LeaderService {
    @Insert("insert into job.alljobs values(#{id},#{name},#{category},#{param},#{scheduleType},#{scheduleParam},#{priority})")
    void addJob(JobInfo jobInfo);
    @Insert("insert into job.alljobs values(#{result}) where id=#{id}")
    void insert_job_result(Map<String,Object> map);
    @Select("select * from job.alljobs where id=#{id}")
    JobInfo select_job(String id);
    @Select("select id,priority,param,scheduleType,scheduleParam,lastScheduleTime from job.alljobs where category= io")
    List<JobCompareParam> select_io_jobs();
    @Select("select id,priority,param,scheduleType,scheduleParam,lastScheduleTime from job.alljobs where category= storage")
    List<JobCompareParam> select_storage_jobs();
    @Select("select id,priority,param,scheduleType,scheduleParam,lastScheduleTime from job.alljobs where category= cpu")
    List<JobCompareParam> select_cpu_jobs();
    @Select("select result from job.alljobs where id=#{id}")
    String select_job_result(int id);
    @Delete("delete from job.alljobs where id=#{id}")
    void deleteJob(String id);
    @Select("select * from job.alljobs")
    List<JobInfo> GetAllJob();
}
