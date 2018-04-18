package com.mjoys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WechatCloudClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatCloudClientApplication.class, args);
		try {
			new Client().start("localhost",8888);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
