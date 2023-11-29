package com.choi.elastic_job.init;
import com.choi.elastic_job.demo.Demo;
import com.choi.elastic_job.service.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class Initialize implements ApplicationRunner {
    @Autowired
    private Server server;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        server.modelChoose(new Demo().init());
    }
}
