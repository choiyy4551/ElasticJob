package com.choi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;
@Configuration
public class JedisConfig {
    @Bean
    public JedisCluster jedisCluster() {
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.17.128", 7001));
        nodes.add(new HostAndPort("192.168.17.128", 7002));
        nodes.add(new HostAndPort("192.168.17.128", 7003));
        nodes.add(new HostAndPort("192.168.17.128", 7004));
        nodes.add(new HostAndPort("192.168.17.128", 7005));
        nodes.add(new HostAndPort("192.168.17.128", 7006));
        return new JedisCluster(nodes);
    }
}
