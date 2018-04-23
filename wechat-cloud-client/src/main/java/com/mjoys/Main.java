package com.mjoys;

import java.net.InetAddress;


public class Main {
    public static void main(String[] args) {
        try {

//            InetAddress addr = InetAddress.getByName("wechat.mjoys.com");
//            String hostAddress = addr.getHostAddress();
            Client client = new Client().start("192.168.1.28", 8888, "wxid_bpz9zdhjx4mm12", "default", 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
