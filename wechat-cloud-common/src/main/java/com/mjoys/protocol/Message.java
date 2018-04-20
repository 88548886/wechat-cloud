package com.mjoys.protocol;


public class Message {

    private int flag;
    private int type;

    private String body;

    public Message(int flag, int type, String body) {
        this.flag = flag;
        this.type = type;
        this.body = body;
    }

    public int getFlag() {
        return flag;
    }

    public Message setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    public int getType() {
        return type;
    }

    public Message setType(int type) {
        this.type = type;
        return this;
    }


    public String getBody() {
        return body;
    }

    public Message setBody(String body) {
        this.body = body;
        return this;
    }
}
