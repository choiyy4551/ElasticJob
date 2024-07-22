package com.choi.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ScheduleType {
    One(1), //即时任务
    Repeat(2), //定时任务
    Daily(3); //每日任务
    private int value;
}
