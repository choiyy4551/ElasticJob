package com.choi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobInfo {
    private String uuid; //uuid
    private String name; //任务名称
    private String param; //任务参数
    private String scheduleType; //任务调度类型
    private String scheduleParam; //调度参数,分钟数
    private String lastRunTime; //最后一次执行时间
    private String runTime; //执行时间持久化
    private int deleteStatus = 0; //0代表任务启用中，1表示任务中止
}
