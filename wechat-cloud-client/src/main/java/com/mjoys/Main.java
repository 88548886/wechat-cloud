package com.mjoys;

import com.alibaba.fastjson.JSON;
import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import com.mjoys.protocol.message.report.WechatAddFriendReport;


import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client().start(300);
            //TODO 发送一个报文
            Thread.sleep(1000);
            client.send(new Message(MessageFlag.MESSAGE_FLAG_REP.getCode(), MessageType.REP_ADD_WECHAT_FRIEND.getCode(),
                    JSON.toJSONString(new WechatAddFriendReport(5, WechatAddFriendReport.RESULT_LIMITED))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
