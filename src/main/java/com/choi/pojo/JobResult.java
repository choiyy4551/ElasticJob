package com.choi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResult {
    private Integer id; //自增id
    private String uuid;
    private String result; //任务执行结果
    private Date startTime; //任务开始时间
    private Date finishTime; //任务完成时间
    private String nodeId; //执行任务的节点id
}