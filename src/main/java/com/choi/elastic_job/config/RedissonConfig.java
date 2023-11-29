package com.choi.elastic_job.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RedissonConfig {
    private String[] redis_ip;
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useClusterServers().setScanInterval(2000).addNodeAddress(redis_ip);
        return Redisson.create(config);
    }
    public void GetRedisIp(String[] redis_ip){
        this.redis_ip=redis_ip;
    }
}
