package com.mjoys;

import com.mjoys.po.Task;
import com.mjoys.service.IRedisService;
import com.mjoys.service.ITaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {
    @Autowired
    private ITaskService taskService;

    @Test
    public void test() {
        taskService.findAll();
    }

}
