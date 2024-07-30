package com.choi.mapper;

import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseMapper {
    @Insert("insert into jobinfo(uuid, name, param,scheduleType, scheduleParam, runTime)" +
            "values (#{uuid}, #{name}, #{param}, #{scheduleType}, #{scheduleParam}, #{runTime})")
    boolean addJobInfo(JobInfo jobInfo);
    @Update("update jobinfo set name = #{name}, param = #{param}, scheduleType = #{scheduleType}, scheduleParam = #{scheduleParam} where uuid = #{uuid}")
    boolean updateJobInfo(JobInfo jobInfo);
    @Select("select * from jobinfo")
    List<JobInfo> getAllJob();
    @Select("select * from jobinfo where deleteStatus = 0")
    List<JobInfo> getAllUsefulJob();
    @Select("select * from jobinfo where uuid = #{uuid}")
    JobInfo getJobById(String uuid);
    @Update("update jobinfo set lastRunTime = #{lastRunTime} where uuid = #{uuid}")
    boolean setLastRunTime(@Param("lastRunTime")String lastRunTime, @Param("uuid")String uuid);
    @Update("update jobinfo set deleteStatus = 0 where name = #{name}")
    boolean startJob(String name);
    @Update("update jobinfo set deleteStatus = 1 where name = #{name}")
    boolean stopJob(String name);
    @Delete("delete from jobinfo where name = #{name}")
    boolean deleteJob(String name);

    @Select("select * from jobresult where uuid = #{uuid}")
    JobResult getResultById(String uuid);
    @Delete("delete from jobresult where name = #{name}")
    boolean deleteResult(String name);
    @Update("update jobresult set deleteStatus = 0 where name = #{name}")
    boolean startResult(String name);
    @Update("update jobresult set deleteStatus = 1 where name = #{name}")
    boolean stopResult(String name);
    @Insert("insert into jobresult(uuid, name)" +
            "values (#{uuid}, #{name})")
    boolean addResult(JobResult jobResult);
    @Select("select * from jobresult")
    List<JobResult> getAllResult();
    @Select("select * from jobresult where deleteStatus = 0")
    List<JobResult> getAllUsefulResult();
    @Update("update jobresult set jobStatus = #{jobStatus} where uuid = #{uuid}")
    boolean setStatus(@Param("jobStatus")int jobStatus, @Param("uuid")String uuid);
    @Update("update jobresult set failureTimes = #{failureTimes} where uuid = #{uuid}")
    void setFailureTimes(int failureTimes, String uuid);
    @Update("update jobresult set result = #{result}, jobStatus = #{jobStatus}, startTime = #{startTime}, " +
            "finishTime = #{finishTime}, nodeId = #{nodeId}, failureTimes = #{failureTimes} where uuid = #{uuid}")
    boolean updateResult(JobResult jobResult);
}
