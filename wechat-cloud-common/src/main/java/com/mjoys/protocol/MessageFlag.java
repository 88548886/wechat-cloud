package com.mjoys.protocol;

public enum MessageFlag {
    MESSAGE_FLAG_SYS(1),
    MESSAGE_FLAG_BUS(2);

    private int code;

    MessageFlag(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public MessageFlag setCode(int code) {
        this.code = code;
        return this;
    }
    public static MessageFlag build(int code){
        for(MessageFlag value:MessageFlag.values()){
            if(value.getCode() == code){
                return value;
            }
        }
        throw new IllegalArgumentException();
    }
}
