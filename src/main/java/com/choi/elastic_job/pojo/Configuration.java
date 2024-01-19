package com.choi.elastic_job.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
    private String clustername;   //集群名称
    private String hostIp;  //输入本地ip
    private Integer hostPort;   //端口号默认4551
}
