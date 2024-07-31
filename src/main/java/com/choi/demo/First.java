package com.choi.demo;

import com.choi.pojo.Configuration;
import com.choi.pojo.JobInfo;
import com.choi.service.client.SlaveService;
import com.choi.utils.StringIntegerUtil;
import org.springframework.stereotype.Component;

@Component
public class First {
    public Configuration init() {
        Configuration configuration = new Configuration();
        configuration.setClusterName("cluster");
        configuration.setResources("100");
        configuration.setIp("127.0.0.1");
        configuration.setPort(8080);
        configuration.setMaxParallel(4);
        return configuration;
    }
    public static class Demo01 implements SlaveService {
        @Override
        public boolean runJob(JobInfo jobInfo) {
            int param = StringIntegerUtil.StringToInteger(jobInfo.getParam());
            try {
                Thread.sleep(param);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
    }
}
