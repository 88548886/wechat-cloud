package com.mjoys.protocol.message.business;


import com.mjoys.protocol.MessageBody;

public class SendSmsMsgRequest extends MessageBody {
    private String uid;

    public SendSmsMsgRequest(String uid) {
        this.uid = uid;
    }
}
