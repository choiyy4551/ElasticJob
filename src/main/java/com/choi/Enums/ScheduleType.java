package com.choi.Enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum ScheduleType {
    AtOnce(1), //即时任务
    Time(2), //定时任务
    Daily(3); //每日任务
    private int value;
}
