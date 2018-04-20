package com.mjoys;

import com.alibaba.fastjson.JSON;
import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import com.mjoys.protocol.message.business.TaskRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class WechatCloudClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatCloudClientApplication.class, args);
        List<ServerNode> serverList = Server.getServerList();
        try {
            //TODO 随机选
            ServerNode serverNode = serverList.get(0);
            System.out.println(serverNode.getHost() + serverNode.getPort());
            Client client = new Client().start(serverNode.getHost(), serverNode.getPort(), 10);
            while (true) {
                client.send(new Message(MessageFlag.MESSAGE_FLAG_SYS.getCode(),
                        MessageType.MESSAGE_TYPE_SYS_TASK.getCode(), JSON.toJSONString(new TaskRequest().
                        setId(1).
                        setGroupId(0).
                        setChannelHashCode(1000).
                        setMessage("我是上海证券通").
                        setProcessor("wx001").
                        setTarget("friend001").
                        setOperationCode(MessageType.MESSAGE_TYPE_BUS_ADD_FRIEND_REQUEST.getCode()))));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
