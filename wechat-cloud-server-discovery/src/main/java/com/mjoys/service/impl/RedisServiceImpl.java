package com.mjoys.service.impl;

import com.mjoys.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public void set(String key, String value, long expireTime, TimeUnit expireTimeUnit) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, expireTime, expireTimeUnit);
    }

    @Override
    public String get(String key) {
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        return stringValueOperations.get(key);
    }
    @Override
    public void hset(String key, String hashKey, String value, long expireTime, TimeUnit expireTimeUnit) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        opsForHash.put(key, hashKey, value);
        redisTemplate.expire(key, expireTime, expireTimeUnit);
    }
    @Override
    public void hset(String key, String hashKey, String value) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        opsForHash.put(key, hashKey, value);
    }
    @Override
    public String hget(String key, String hashKey) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hashKey);
    }
    @Override
    public Map<String,String> hgetAll(String key) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        return opsForHash.entries(key);
    }


    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public long incr(String key, long expireTime, TimeUnit expireTimeUnit) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        long counter = valueOperations.increment(key, 1);
        if (counter == 1) {
            redisTemplate.expire(key, expireTime, expireTimeUnit);
        }
        return counter;
    }

}
