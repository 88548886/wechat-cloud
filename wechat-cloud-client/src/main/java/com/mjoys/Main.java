package com.mjoys;

import java.net.InetAddress;


public class Main {
    public static void main(String[] args) {
        try {

            InetAddress addr = InetAddress.getByName("wechat.mjoys.com");
            String hostAddress = addr.getHostAddress();
            Client client = new Client().start(hostAddress, 9999, "15968899802", "default", 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
