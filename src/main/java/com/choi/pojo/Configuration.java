package com.choi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
    private String clusterName;
    private String ip;
    private int port;
    private String resources;
    private int maxParallel;
}
