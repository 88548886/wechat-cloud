package com.mjoys.protocol.message.business;


import com.mjoys.protocol.MessageBody;

public class WehcatAddFriendResponse extends MessageBody {
    private String uid;

    public WehcatAddFriendResponse(String uid) {
        this.uid = uid;
    }
}
