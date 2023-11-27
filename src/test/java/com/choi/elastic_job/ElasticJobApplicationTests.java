package com.choi.elastic_job;

import com.choi.elastic_job.pojo.JobCompareParam;
import com.choi.elastic_job.pojo.JobInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SpringBootTest
class ElasticJobApplicationTests {

    @Test
    void contextLoads() {
        Date date=null;
        List<JobCompareParam> list = new ArrayList<JobCompareParam>(){{

        }
        };

        Collections.sort(list);
        list.forEach(p->{
            System.out.println(p);
        });
    }

}
