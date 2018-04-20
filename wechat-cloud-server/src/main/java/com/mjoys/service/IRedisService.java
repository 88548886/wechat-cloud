package com.mjoys.service;

import java.util.concurrent.TimeUnit;

public interface IRedisService {
    void set(String key, String value, long expireTime, TimeUnit expireTimeUnit);

    String get(String key);

    void delete(String key);

    long incr(String key, long expireTime, TimeUnit expireTimeUnit);
}
