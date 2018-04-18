package com.mjoys.protocol;

public enum MessageType {

    /*系统心跳*/
    MESSAGE_TYPE_SYS_HEARTBEAT(0),
    /*通用指令执行状态*/
    MESSAGE_TYPE_BUS_COMMON_SUCCESS(1),
    MESSAGE_TYPE_BUS_COMMON_FAILURE(-1),
    /*登陆请求*/
    MESSAGE_TYPE_BUS_LOGIN_REQUEST(11001),
    MESSAGE_TYPE_BUS_LOGIN_RESPONSE(11002),
    MESSAGE_TYPE_BUS_ADD_FRIEND_REQUEST(12001),
    MESSAGE_TYPE_BUS_ADD_FRIEND_RESPONSE(12002);
    private int code;

    MessageType(int value) {
        this.code = value;
    }

    public int getCode() {
        return code;
    }

    public MessageType setCode(int code) {
        this.code = code;
        return this;
    }
}
