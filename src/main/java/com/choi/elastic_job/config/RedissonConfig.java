package com.choi.elastic_job.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
@Component
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useClusterServers().setScanInterval(2000).addNodeAddress("redis://127.0.0.1:6370","redis://127.0.0.1:6371","redis://127.0.0.1:6372");
        return Redisson.create(config);
    }
}
