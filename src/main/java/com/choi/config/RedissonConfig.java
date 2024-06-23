package com.choi.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useClusterServers().addNodeAddress("redis://192.168.17.128:7001", "redis://192.168.17.128:7002",
                "redis://192.168.17.128:7003", "redis://192.168.17.128:7004", "redis://192.168.17.128:7005", "redis://192.168.17.128:7006");
        return Redisson.create(config);
    }
}
