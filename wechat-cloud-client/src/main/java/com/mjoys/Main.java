package com.mjoys;

import com.alibaba.fastjson.JSON;
import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import com.mjoys.protocol.message.report.WechatAddFriendReport;
import com.mjoys.protocol.message.system.Task;


import java.net.InetAddress;
import java.net.URL;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {

            InetAddress addr = InetAddress.getByName("wechat.cloud.mjoys.com");
            String hostAddress = addr.getHostAddress();

            Client client = new Client().start(hostAddress, 9999, "System", "default", 300);
            //TODO 发送一个报文
            Thread.sleep(1000);

            Message msg = new Message(MessageFlag.MESSAGE_FLAG_SYS.getCode(), MessageType.SYS_TASK.getCode(),
                    JSON.toJSONString(new Task().
                            setCommandType(MessageType.COM_ADD_WECHAT_FRIEND.getCode()).
                            setExecuteTime(-1).
                            setId(1).
                            setReceiver("88548886").
                            setTerminalAddr("192.168.1.117:44408").
                            setBussinessId(0).
                            setMessage("我是上海证券通")
                    ));
            MessageWarp messageWarp = new MessageWarp().setMsg(msg).setReceiver(new MessageWarp.Receiver().setIp
                    ("192.168.1.210").setPort(9999));
            client.submitMsg(messageWarp);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
