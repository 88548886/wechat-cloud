package com.mjoys;

import com.mjoys.service.IRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {
    @Autowired
    private IRedisService redisService;

    @Test
    public void test() {

        redisService.set("test", "test", 10, TimeUnit.MINUTES);
        System.out.println(redisService.get("test"));
    }

}
