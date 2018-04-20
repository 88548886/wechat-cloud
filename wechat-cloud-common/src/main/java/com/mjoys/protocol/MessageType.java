package com.mjoys.protocol;

public enum MessageType {

    /*系统心跳*/
    MESSAGE_TYPE_SYS_HEARTBEAT(0),
    MESSAGE_TYPE_SYS_TASK(1),
    /*通用指令执行状态*/
    MESSAGE_TYPE_BUS_COMMON_SUCCESS(200),
    MESSAGE_TYPE_BUS_COMMON_FAILURE(500),
    /*登陆请求*/
    MESSAGE_TYPE_BUS_LOGIN_REQUEST(10011),
    MESSAGE_TYPE_BUS_LOGIN_RESPONSE(10012),

    /*手机发短信*/
    MESSAGE_TYPE_BUS_SEND_MSG_REQUEST(10021),
    MESSAGE_TYPE_BUS_SEND_MSG_RESPONSE(10022),

    /*微信添加好友*/
    MESSAGE_TYPE_BUS_ADD_FRIEND_REQUEST(10031),
    MESSAGE_TYPE_BUS_ADD_FRIEND_RESPONSE(10032);
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

    public static MessageType build(int code){
        for(MessageType value:MessageType.values()){
            if(value.getCode() == code){
                return value;
            }
        }
        throw new IllegalArgumentException();
    }
}
