package com.mjoys;

import com.mjoys.service.IRedisService;
import com.mjoys.service.ITaskService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest

public class WechatCloudServerApplicationTests {
    @Autowired
    private ITaskService taskService;

    @Autowired
    private IRedisService redisService;
    @Ignore
    @Test
    public void testTaskService() {
        taskService.markTaskAsSubmitted(1);
        taskService.markTaskAsExecuted(2);
        taskService.updateTaskResult(3,1);
    }



    @Test
    public void testRedisService() {

//        redisService.hset("wx-server", "a", "a");
//        redisService.hset("wx-server", "b", "b");
//        redisService.hset("wx-server", "c", "c");
//
//        redisService.hgetAll("wx-server").forEach((i, j) -> System.out.println(i + "\t" + j));
//
//        System.out.println("-----------------------");

        List<String> hashKeys = new ArrayList<>();
        hashKeys.add("wxid1");
        hashKeys.add("wxid2");
        List<String> strings = redisService.multiGet("wechat-cloud:client:addr",hashKeys);
        strings.forEach(i -> System.out.println(i));
    }
}
