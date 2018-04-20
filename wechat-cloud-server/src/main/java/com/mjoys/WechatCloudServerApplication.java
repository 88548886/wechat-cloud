package com.mjoys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WechatCloudServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(WechatCloudServerApplication.class, args);
        try {
            new Server().start(8888);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
