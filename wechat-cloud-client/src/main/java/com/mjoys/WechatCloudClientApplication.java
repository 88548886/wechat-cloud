package com.mjoys;

import com.alibaba.fastjson.JSON;
import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import com.mjoys.protocol.message.report.WechatAddFriendReport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class WechatCloudClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatCloudClientApplication.class, args);
        List<ServerNode> serverList = Server.getServerList();
        try {
            //TODO 随机选一个服务节点
            ServerNode serverNode = serverList.get(0);
            System.out.println(serverNode.getHost() + serverNode.getPort());
            Client client = new Client().start(serverNode.getHost(), serverNode.getPort(), 10);
            //TODO 发送一个报文
            client.send(new Message(MessageFlag.MESSAGE_FLAG_REP.getCode(), MessageType.REP_ADD_WECHAT_FRIEND.getCode(),
                    JSON.toJSONString(new WechatAddFriendReport(1, WechatAddFriendReport.RESULT_LIMITED))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
