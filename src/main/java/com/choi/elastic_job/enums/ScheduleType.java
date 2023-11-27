package com.choi.elastic_job.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ScheduleType {
    One(0),
    Repeat(1),
    Daily(2),
    ;
    private Integer value;
}
