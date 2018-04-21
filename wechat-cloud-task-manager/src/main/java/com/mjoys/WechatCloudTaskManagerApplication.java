package com.mjoys;

import com.mjoys.service.IAccountService;
import com.mjoys.service.IRedisService;
import com.mjoys.service.ITaskService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class WechatCloudTaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatCloudTaskManagerApplication.class, args);
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//		scheduledExecutorService.scheduleAtFixedRate(new TaskManager(),0,1, TimeUnit.MINUTES);
		new Thread(new TaskManager()).start();
	}
}
