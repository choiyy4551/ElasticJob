package com.choi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeJob {
    private String uuid;
    private String name;
    private String param;
    private String scheduleParam;
    private int deleteStatus;
}
