package com.choi.elastic_job.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRunParam {
    private String id;
    private String category;
    private String param;
}
