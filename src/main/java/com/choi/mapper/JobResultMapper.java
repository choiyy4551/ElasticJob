package com.choi.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.choi.pojo.JobResult;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@DS("db1")
public interface JobResultMapper {
    @Select("select * from jobresult where uuid = #{uuid}")
    JobResult getJobResultById(String uuid);
    @Delete("delete from jobresult where name = #{name}")
    boolean deleteJob(String name);
    @Update("update jobresult set deleteStatus = 0 where name = #{name}")
    boolean startJob(String name);
    @Update("update jobresult set deleteStatus = 1 where name = #{name}")
    boolean stopJob(String name);
    @Insert("insert into jobresult(uuid, name)" +
            "values (#{uuid}, #{name})")
    boolean addResult(JobResult jobResult);
    @Select("select * from jobresult")
    List<JobResult> getAllJobResult();
    @Select("select * from jobresult where name = #{name}")
    JobResult getJobResult(String name);
    @Update("update jobresult set jobStatus = #{jobStatus} where uuid = #{uuid}")
    boolean setJobStatus(@Param("jobStatus")int jobStatus, @Param("uuid")String uuid);
    @Select("select jobStatus from jobresult where uuid = #{uuid}")
    int getJobStatus(String uuid);
    @Update("update jobresult set result = #{result}, jobStatus = #{jobStatus}, startTime = #{startTime}, " +
            "finishTime = #{finishTime}, nodeId = #{nodeId}, failureTimes = #{failureTimes} where uuid = #{uuid}")
    boolean updateJobResult(JobResult jobResult);
}
