package com.choi.mapper;

import com.choi.pojo.JobResult;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobResultMapper {
    @Select("select * from jobresult where uuid = #{uuid}")
    JobResult getJobResultById(String uuid);
    @Delete("delete from jobresult where name = #{name}")
    boolean deleteJob(String name);
    /**
     * 任务完成添加执行结果
     * @param jobResult 任务结果信息
     * @return boolean
     */
    @Insert("insert into jobresult(uuid, name)" +
            "values (#{uuid}, #{name})")
    boolean addResult(JobResult jobResult);
    @Select("select * from jobresult")
    List<JobResult> getAllJobResult();
    @Update("update jobresult set jobStatus = #{jobStatus} where uuid = #{uuid}")
    boolean setJobStatus(@Param("jobStatus")int jobStatus, @Param("uuid")String uuid);
    @Select("select jobStatus from jobresult where uuid = #{uuid}")
    int getJobStatus(String uuid);
    @Select("select jobStatus from jobresult where name = #{name}")
    int getJobStatusByName(String name);
    @Update("update jobresult set result = #{result}, jobStatus = #{jobStatus}, startTime = #{startTime}, " +
            "finishTime = #{finishTime}, nodeId = #{nodeId}, failureTimes = #{failureTimes} where uuid = #{uuid}")
    boolean updateJobResult(JobResult jobResult);
}
