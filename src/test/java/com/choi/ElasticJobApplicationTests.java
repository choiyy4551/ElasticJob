package com.choi;

import com.choi.mapper.JobMapper;
import com.choi.mapper.JobResultMapper;
import com.choi.pojo.JobInfo;
import com.choi.pojo.JobResult;
import com.choi.utils.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootTest
class ElasticJobApplicationTests {
    @Autowired
    JobMapper jobMapper;
    @Autowired
    JobResultMapper jobResultMapper;
    @Test
    void getAllJob() throws ParseException {
        JobResult jobResult = new JobResult();
        jobResult.setUuid("42a5f504a7f54f669396a8703bea63bc");
        jobResult.setFinishTime("dwdww");
        jobResultMapper.updateJobResult(jobResult);
    }
}
