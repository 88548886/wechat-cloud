package com.mjoys.protocol.message.business;


import com.mjoys.protocol.MessageBody;

public class SendSmsMsgResponse extends MessageBody {
    private String uid;

    public SendSmsMsgResponse(String uid) {
        this.uid = uid;
    }
}
