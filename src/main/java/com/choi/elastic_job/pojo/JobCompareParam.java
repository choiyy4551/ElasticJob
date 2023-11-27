package com.choi.elastic_job.pojo;

import com.choi.elastic_job.utils.ScheduleTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Data
@NoArgsConstructor
@Component
public class JobCompareParam implements Comparable<JobCompareParam>{
    private String id;
    private String param;
    private String priority;
    private String scheduleType;
    private String scheduleParam;
    private Date lastScheduleTime;
    private Date date;
    @Autowired
    private ScheduleTime scheduleTime;
    public JobCompareParam(String id, String param, String priority, String scheduleType, String scheduleParam,Date lastScheduleTime) {
        this.id = id;
        this.param = param;
        this.priority = priority;
        this.scheduleType = scheduleType;
        this.scheduleParam = scheduleParam;
        this.date=scheduleTime.GetNextScheduleTime(scheduleType,scheduleParam,lastScheduleTime);
    }

    @Override
    public int compareTo(JobCompareParam o) {
        if(o.date.compareTo(this.date)>0){
            return -1;
        }else if(o.date.compareTo(this.date)<0){
            return 1;
        }else {
            if (o.getPriority().compareTo(this.getPriority()) > 0) {
                return -1;
            } else if (o.getPriority().compareTo(this.getPriority()) == 0) {
                if (o.getParam().compareTo(this.getParam()) > 0) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        }
    }
}
