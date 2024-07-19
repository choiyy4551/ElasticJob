package com.choi.mapper;


import com.choi.pojo.*;
import lombok.Data;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobMapper {
    /**
     * 添加立刻执行的任务
     * @param jobInfo 任务信息
     * @return boolean
     */
    @Insert("insert into atonce_job(uuid, name, param, deleteStatus)" +
            "values (#{uuid}, #{name}, #{param}, #{deleteStatus})")
    boolean addAtOnceJob(JobInfo jobInfo);

    /**
     * 添加每日任务
     * @param jobInfo 任务信息
     * @return boolean
     */
    @Insert("insert into daily_job(uuid, name, param, scheduleParam, deleteStatus)" +
            "values (#{uuid}, #{name}, #{param}, #{scheduleParam}, #{deleteStatus})")
    boolean addDailyJob(JobInfo jobInfo);

    /**
     * 添加定时任务
     * @param jobInfo 任务信息
     * @return boolean
     */
    @Insert("insert into time_Job(uuid, name, param, scheduleParam, deleteStatus)" +
            "values (#{uuid}, #{name}, #{param}, #{scheduleParam}, #{deleteStatus})")
    boolean addTimeJob(JobInfo jobInfo);

    @Insert("insert into jobinfo(uuid, name, param,scheduleType, scheduleParam, runTime)" +
            "values (#{uuid}, #{name}, #{param}, #{scheduleType}, #{scheduleParam}, #{runTime})")
    boolean addJobInfo(JobInfo jobInfo);
    @Update("update jobinfo set name = #{name}, param = #{param}, scheduleType = #{scheduleType}, scheduleParam = #{scheduleParam} where uuid = #{uuid}")
    boolean updateJobInfo(JobInfo jobInfo);
    @Select("select * from jobinfo")
    List<JobInfo> getAllJob();
    @Select("select * from jobinfo where name = #{name}")
    JobInfo getJobByName(String name);
    @Select("select * from jobinfo where uuid = #{uuid}")
    JobInfo getJobById(String uuid);
    @Update("update jobinfo set lastRunTime = #{lastRunTime} where uuid = #{uuid}")
    boolean setLastRunTime(@Param("lastRunTime")String lastRunTime,@Param("uuid")String uuid);
    @Update("update jobinfo set deleteStatus = 0 where uuid = #{uuid}")
    boolean startJob(String uuid);
    @Update("update jobinfo set deleteStatus = 1 where uuid = #{uuid}")
    boolean stopJob(String uuid);
    @Delete("delete from jobinfo where name = #{name}")
    boolean deleteJob(String name);



    @Select("select * from time_job where deleteStatus = 0")
    List<TimeJob> getAllTimeJob();
    @Select("select * from daily_job where deleteStatus = 0")
    List<DailyJob> getAllDailyJob();
    @Select("select * from atonce_job where deleteStatus = 0")
    List<AtOnceJob> getAllAtOnceJob();
}
