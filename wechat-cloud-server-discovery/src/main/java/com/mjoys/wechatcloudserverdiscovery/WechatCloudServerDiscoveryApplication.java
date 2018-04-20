package com.mjoys.wechatcloudserverdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WechatCloudServerDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatCloudServerDiscoveryApplication.class, args);
	}
	@GetMapping("/discovery")
	public  String discovery(){
		return "[{\"host\":\"192.168.1.28\",\"port\":8888}]";
	}
}
