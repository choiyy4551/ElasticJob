package com.choi.pojo;

import com.choi.Enums.JobStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobTimeInfo implements Comparable<JobTimeInfo> {
    private JobInfo jobInfo;
    private JobStatusEnum status = JobStatusEnum.Waiting;
    private Date runTime; //用于执行时间判断
    @Override
    public int compareTo(JobTimeInfo other) {
        return this.runTime.compareTo(other.runTime);
    }
}
