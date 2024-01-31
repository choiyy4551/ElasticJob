package com.choi.elastic_job;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.choi.elastic_job.service.LeaderService")
public class ElasticJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticJobApplication.class, args);

    }

}