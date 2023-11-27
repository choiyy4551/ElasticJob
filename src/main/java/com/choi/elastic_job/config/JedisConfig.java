package com.choi.elastic_job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
@Component
public class JedisConfig {
    @Bean
    public Jedis jedisConfig(){
        return new Jedis("127.0.0.1");
    }
}
