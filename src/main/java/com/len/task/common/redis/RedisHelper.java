package com.len.task.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author sujianfeng
 */
@Service
public class RedisHelper {

    private static final Logger log = LoggerFactory.getLogger("redis_log");

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    @Qualifier("redisTemplateSlave")
    private RedisTemplate redisTemplateSlave;

    @Autowired
    @Qualifier("redisTemplateSlave2")
    private RedisTemplate redisTemplateSlave2;

    private Random random = new Random(1);

    public void set(String key, Object value, int timeout, TimeUnit unit) {
        try {
            key = String.valueOf(key.hashCode());
            RedisSerializer redisSerializer = new StringRedisSerializer();
            redisTemplate.setKeySerializer(redisSerializer);
            ValueOperations<String, Object> vo = redisTemplate.opsForValue();
            vo.set(key, value, timeout, unit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object get(String key) {
        key = String.valueOf(key.hashCode());

        int num = random.nextInt(2);
        ValueOperations<String, Object> vo;

        long start = System.currentTimeMillis();
        if (num == 1) {
            vo = redisTemplateSlave.opsForValue();
        } else {
            vo = redisTemplateSlave2.opsForValue();
        }
        Object value = null;
        try {
            value = vo.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        log.info("从redis{}读取到数据，花费时间{}", num, (end - start));
        return value;
    }

}
