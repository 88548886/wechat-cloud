package com.mjoys;

import com.alibaba.fastjson.JSON;
import com.mjoys.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RestController
public class WechatCloudServerDiscoveryApplication {
    @Autowired
    private IRedisService redisService;

    public static void main(String[] args) {
        SpringApplication.run(WechatCloudServerDiscoveryApplication.class, args);
    }

    @GetMapping("/discovery")
    public String discovery() {
        Map<String, String> serversCacheMap = redisService.hgetAll("wechat-cloud:servers");
        List<ServerNode> serverNodeList = new ArrayList<>();
        if (null != serversCacheMap && serversCacheMap.size() > 0) {
            serversCacheMap.forEach((k, v) -> {
                long latest = Long.parseLong(v);
                //server节点最后的刷新时间在10秒内则有效
                if ((System.currentTimeMillis() - latest) < TimeUnit.SECONDS.toMillis(10)) {
                    String[] hostPort = k.split(":");
                    serverNodeList.add(new ServerNode().setHost(hostPort[0]).setPort(Integer.parseInt(hostPort[1])));
                }
            });
        }
        return JSON.toJSONString(serverNodeList);
    }
}
