package com.mjoys;

import com.mjoys.service.IAccountService;
import com.mjoys.service.IRedisService;
import com.mjoys.service.ITaskService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class WechatCloudTaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatCloudTaskManagerApplication.class, args);

	}
}
