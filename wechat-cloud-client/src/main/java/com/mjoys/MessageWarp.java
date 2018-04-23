package com.mjoys;


import com.mjoys.protocol.Message;

public class MessageWarp {
    private Message msg;
    private Receiver receiver;

    public Message getMsg() {
        return msg;
    }

    public MessageWarp setMsg(Message msg) {
        this.msg = msg;
        return this;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public MessageWarp setReceiver(Receiver receiver) {
        this.receiver = receiver;
        return this;
    }

    public static class Receiver {
        public String ip;
        public int port;

        public String getIp() {
            return ip;
        }

        public Receiver setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public int getPort() {
            return port;
        }

        public Receiver setPort(int port) {
            this.port = port;
            return this;
        }
    }
}
