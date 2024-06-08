package com.choi;

import com.choi.mapper.JobMapper;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class ElasticJobApplicationTests {
    @Autowired
    JobMapper jobMapper;
    @Test
    public void addResult(){
        JobResult jobResult = new JobResult(10, "3", "success", new Date(), new Date(), "1");
        jobMapper.AddResult(jobResult);
    }
}
