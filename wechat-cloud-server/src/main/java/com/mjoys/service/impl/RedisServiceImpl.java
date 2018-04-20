package com.mjoys.service.impl;

import com.mjoys.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public void set(String key, String value, long expireTime, TimeUnit expireTimeUnit) {
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        stringValueOperations.set(key, value, expireTime, expireTimeUnit);
    }

    @Override
    public String get(String key) {
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        return stringValueOperations.get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public long incr(String key, long expireTime, TimeUnit expireTimeUnit) {
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        long counter = stringValueOperations.increment(key, 1);
        if (counter == 1) {
            redisTemplate.expire(key, expireTime, expireTimeUnit);
        }
        return counter;
    }
}
