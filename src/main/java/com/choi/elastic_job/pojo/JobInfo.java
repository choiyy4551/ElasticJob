package com.choi.elastic_job.pojo;

import com.choi.elastic_job.controller.ClientInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobInfo {
    private String id;   //任务id
    private String name;  //任务名称
    private String param;     //任务运行参数
    private String scheduleType; //调度类型
    private String scheduleParam; //调度参数
    private int priority = 0;   //任务优先级
    private Date last_time;     //上次执行时间
    private Date lastScheduleTime; //最后被调度时间
    private ClientInterface clientInterface;
}
