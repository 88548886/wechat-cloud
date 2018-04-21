package com.mjoys.protocol;

public enum MessageType {

    /*系统心跳*/
    SYS_HEARTBEAT(0),
    SYS_TASK(1),
    SYS_COMMAND_RECEIVED_ACK(2),
    SYS_COMMAND_EXECUTED_ACK(3),

    /*手机发短信*/
    COM_SEND_MSG(10021),
    REP_SEND_MSG(10022),

    /*微信添加好友*/
    COM_ADD_WECHAT_FRIEND(10031),
    REP_ADD_WECHAT_FRIEND(10032);
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
        for(MessageType value: MessageType.values()){
            if(value.getCode() == code){
                return value;
            }
        }
        throw new IllegalArgumentException();
    }
}
