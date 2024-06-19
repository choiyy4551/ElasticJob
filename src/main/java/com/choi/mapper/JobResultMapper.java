package com.choi.mapper;

import com.choi.pojo.JobResult;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobResultMapper {
    @Select("select * from job_result where uuid = #{uuid}")
    JobResult getJobResultById(String uuid);
    @Delete("delete from job_result where uuid = #{uuid}")
    boolean deleteJob(String uuid);
    /**
     * 任务完成添加执行结果
     * @param jobResult 任务结果信息
     * @return boolean
     */
    @Insert("insert into job_result(uuid, name, result, jobStatus, startTime, finishTime, nodeId, failureTimes)" +
            "values (#{uuid}, #{name}, #{result},#{jobStatus}, #{startTime}, #{finishTime}, #{nodeId}, #{failureTimes})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    boolean addResult(JobResult jobResult);
    @Select("select * from job_result")
    List<JobResult> getAllJobResult();
    @Update("update job_result set status = #{status} where uuid = #{uuid} ")
    boolean setJobStatus(int status, String uuid);
    @Select("select status from job_result where uuid = #{uuid}")
    int getJobStatus(String uuid);
    @Select("select status from job_result where name = #{name}")
    int getJobStatusByName(String name);
    @Update("update job_result set result = #{result}, jobStatus = #{jobStatus}, startTime = #{startTime}, " +
            "finishTime = #{finishTime}, nodeId = #{NodeId}, failureTimes = #{failureTimes}")
    boolean updateJobResult(JobResult jobResult);
}
