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
    @Insert("insert into atonce_job(uuid, name, param, deletestatus)" +
            "values (#{uuid}, #{name}, #{param}, #{deleteStatus})")
    boolean addAtOnceJob(JobInfo jobInfo);

    /**
     * 添加每日任务
     * @param jobInfo 任务信息
     * @return boolean
     */
    @Insert("insert into daily_job(uuid, name, param, scheduleparam, deletestatus)" +
            "values (#{uuid}, #{name}, #{param}, #{scheduleParam}, #{deleteStatus})")
    boolean addDailyJob(JobInfo jobInfo);

    /**
     * 添加定时任务
     * @param jobInfo 任务信息
     * @return boolean
     */
    @Insert("insert into time_Job(uuid, name, param, scheduleparam, deletestatus)" +
            "values (#{uuid}, #{name}, #{param}, #{scheduleParam}, #{deleteStatus})")
    boolean addTimeJob(JobInfo jobInfo);

    @Insert("insert into JobInfo(uuid, name, param, lastRunTime,scheduleType, scheduleParam, deleteStatus)" +
            "values (#{uuid}, #{name}, #{param},#{lastRunTime}, #{scheduleType}, #{scheduleParam}, #{deleteStatus})")
    boolean addJobInfo(JobInfo jobInfo);
    @Update("update JobInfo set name = #{name}, param = #{param}, scheduleParam = #{scheduleType}, deleteStatus = #{deleteStatus} where uuid = #{uuid}")
    boolean updateJobInfo(JobInfo jobInfo);
    @Select("select * from JobInfo where deleteStatus = 0")
    List<JobInfo> getAllJob();
    @Select("select * from JobInfo where name = #{name} and deleteStatus = 0 limit 1")
    JobInfo getJobByName(String name);
    @Select("select * from JobInfo where uuid = #{uuid}")
    JobInfo getJobById(String uuid);
    @Update("update JobInfo set deleteStatus = 0 where uuid = #{uuid}")
    boolean startJob(String uuid);
    @Update("update JobInfo set deleteStatus = 1 where uuid = #{uuid}")
    boolean stopJob(String uuid);
    @Select("select * from time_job where deletestatus = 0")
    List<TimeJob> getAllTimeJob();

    @Select("select * from daily_job where deletestatus = 0")
    List<DailyJob> getAllDailyJob();

    @Select("select * from job_result where finishtime is null or finishtime = 0")
    List<JobResult> getUnfinishedJob();
    @Select("select * from atonce_job where deletestatus = 0")
    List<AtOnceJob> getAllAtOnceJob();
}
