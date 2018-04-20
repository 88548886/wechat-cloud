package com.mjoys.protocol;

public enum MessageFlag {
    MESSAGE_FLAG_SYS(1),
    MESSAGE_FLAG_COM(2),
    MESSAGE_FLAG_REP(3);

    private int code;

    MessageFlag(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MessageFlag build(int code) {
        for (MessageFlag value : MessageFlag.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException();
    }
}
