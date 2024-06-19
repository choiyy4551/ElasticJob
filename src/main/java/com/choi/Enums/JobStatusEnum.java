package com.choi.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum JobStatusEnum {
    Pending(1), // 任务已经触发，但未执行
    Doing(2),   // 任务执行中
    Success(3), // 任务执行成功
    Failed(4),  // 任务执行失败
    Waiting(5),
    Prepared(6)
    ;
    private int value;
}
