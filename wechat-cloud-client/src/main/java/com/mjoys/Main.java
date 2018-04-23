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

//            InetAddress addr = InetAddress.getByName("wechat.cloud.mjoys.com");
//            String hostAddress = addr.getHostAddress();

            Client client = new Client().start("192.168.1.28", 8888, "15968899802", "default", 300);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
