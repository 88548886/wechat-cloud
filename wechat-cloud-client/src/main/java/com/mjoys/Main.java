package com.mjoys;

import com.alibaba.fastjson.JSON;
import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import com.mjoys.protocol.message.report.WechatAddFriendReport;
import com.mjoys.protocol.message.system.Task;


import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client().start("192.168.1.28", 8888, "System", "default", 300);
            //TODO 发送一个报文
            Thread.sleep(1000);
            /*client.send(new Message(MessageFlag.MESSAGE_FLAG_SYS.getCode(), MessageType.SYS_TASK.getCode(),
                    JSON.toJSONString(new Task().
                            setCommandType(MessageType.COM_ADD_WECHAT_FRIEND.getCode()).
                            setExecuteTime(-1).
                            setId(1)
                            .setReceiver("88548886")
                            .setTerminalAddr("192.168.1.117:36646")
                            .setBussinessId(0).setMessage("我是上海证券通")
                    )));*/

//            client.send(new Message(MessageFlag.MESSAGE_FLAG_SYS.getCode(), MessageType.SYS_TASK.getCode(),
//                    JSON.toJSONString(new Task().
//                            setCommandType(MessageType.COM_SEND_MSG.getCode()).
//                            setExecuteTime(-1).
//                            setId(2)
//                            .setReceiver("15968899802")
//                            .setTerminalAddr("192.168.1.189:41383")
//                            .setBussinessId(0).setMessage("我是英启教育")
//                    )));
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
