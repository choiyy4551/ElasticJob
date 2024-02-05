package com.choi.elastic_job.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Component
public class JedisUtils {

    @Autowired
    private JedisPool jedisPool;

    private Jedis jedis;

    /**
     * 获取一个Jedis实例
     */
    public Jedis getInstance() {
        jedis = jedisPool.getResource();
        jedis.select(1); // 选择存储库，单机版默认为db(0)
        return jedis;
    }

    /**
     * 回收Jedis实例
     */
    public void takebackJedis(Jedis jedis) {
        if (jedis != null && jedisPool != null) {
            jedis.close();
        }
    }

    /**
     * 根据key获取Value
     */
    public String get(String key) {
        return jedis.get(key);
    }

    /**
     * 添加键值对
     */
    public String set(String key, String value) {
        // jedie.set(key, value, "NX", "EX", 1800); // 添加key设置TTL
        return jedis.set(key, value);
    }

    /**
     * 删除一个或多个key
     */
    public Long del(String... keys) {
        return jedis.del(keys);
    }


}
