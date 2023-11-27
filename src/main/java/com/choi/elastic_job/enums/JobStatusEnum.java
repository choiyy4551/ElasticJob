package com.choi.elastic_job.enums;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum JobStatusEnum {
    Pending(1), // 任务已经触发，但未执行
    Doing(2),   // 任务执行中
    Success(3), // 任务执行成功
    Failed(4),  // 任务执行失败
    Waiting(5),
    Prepared(6),
    Running(7),
    ;
    private int value;

    public int getValue() {
        return value;
    }

    public static JobStatusEnum valueOf(int index) {
        for (JobStatusEnum statusEnum : values()) {
            if (statusEnum.getValue() == index) {
                return statusEnum;
            }
        }
        return null;
    }
}
