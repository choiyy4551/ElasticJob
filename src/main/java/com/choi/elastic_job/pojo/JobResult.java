package com.choi.elastic_job.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResult {
    private String status; //任务状态
    private String result;  //任务执行结果
}
