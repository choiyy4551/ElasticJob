package com.choi;

import com.choi.mapper.DB1Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElasticJobApplicationTests {
    @Autowired
    private DB1Mapper myDatabaseMapper;
    @Test
    public void mapperTest() {
        myDatabaseMapper.callInsertSequence("烧水");
    }
}
