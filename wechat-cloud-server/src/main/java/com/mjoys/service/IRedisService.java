package com.mjoys.service;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface IRedisService {
    void set(String key, String value, long expireTime, TimeUnit expireTimeUnit);

    String get(String key);

    void delete(String key);

    long incr(String key, long expireTime, TimeUnit expireTimeUnit);


    void hset(String key, String hashKey, String value, long expireTime, TimeUnit expireTimeUnit);

    void hsetAll(String key, Map<String, String> keyValuePairs, long expireTime, TimeUnit expireTimeUnit);

    void hsetAll(String key, Map<String, String> keyValuePairs);

    void hset(String key, String hashKey, String value);

    String hget(String key, String hashKey);

    Map<String, String> hgetAll(String key);

    List<String> multiGet(String key, Collection<String> hashKeys);
}
