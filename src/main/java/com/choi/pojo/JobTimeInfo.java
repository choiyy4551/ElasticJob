package com.choi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobTimeInfo implements Comparable<JobTimeInfo>{
    private JobInfo jobInfo;
    private String uuid;
    private String name;
    private String scheduleType;
    private Date runTime;
    private int deleteStatus;

    @Override
    public int compareTo(JobTimeInfo other) {
        return this.runTime.compareTo(other.runTime);
    }
}
