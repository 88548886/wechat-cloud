package com.mjoys;

import com.alibaba.fastjson.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Server {
    public static List<ServerNode> getServerList() throws Exception {
        try {
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
            Request request = new Request.Builder()
                    .url("http://192.168.1.28:8999/discovery?uid=wxid1")//请求接口。如果需要传参拼接到接口后面。
                    .build();//创建Request 对象
            Response response = client.newCall(request).execute();//得到Response 对象
            if (response.isSuccessful()) {
                String raw = response.body().string();
                List<ServerNode> serverNodes = JSON.parseArray(raw, ServerNode.class);
                if (null != serverNodes && serverNodes.size() > 0) {
                    return serverNodes;
                }
            }
        } catch (IOException e) {
            throw new Exception("没用找到可用的server节点.");
        }
        throw new Exception("没用找到可用的server节点.");
    }

    public static ServerNode random() {
        try {
            List<ServerNode> serverList = Server.getServerList();
            Random random = new Random();
            int randomIndex = random.nextInt(serverList.size() - 1);
            return serverList.get(randomIndex);
        } catch (Exception e) {
            return new ServerNode().setHost("localhost").setPort(8888);
        }
    }
}
