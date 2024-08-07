package com.choi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResult {
    private String uuid;
    private String name;
    private String result; //任务执行结果
    private int jobStatus = 5;
    private String startTime; //任务开始时间
    private String finishTime; //任务完成时间
    private String nodeId; //执行任务的节点id
    private int failureTimes = 0; //执行失败重做次数
    private int deleteStatus = 0; //0代表启用中，1代表中止
}
