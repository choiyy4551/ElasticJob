package com.choi.elastic_job.demo;

import com.choi.elastic_job.pojo.Configuration;

public class Demo {
    public Configuration init(){
        Configuration configuration=new Configuration();
        configuration.setClustername("elastic");
        configuration.setHostIp("127.0.0.1");
        configuration.setHostPort(4551);
        return configuration;
    }
}
