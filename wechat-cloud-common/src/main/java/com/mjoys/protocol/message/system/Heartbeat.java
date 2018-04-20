package com.mjoys.protocol.message.system;

import com.mjoys.protocol.MessageBody;

public class Heartbeat extends MessageBody {

    private String uid;
    private String portrait;

    public Heartbeat(String uid, String portrait) {
        this.uid = uid;
        this.portrait = portrait;
    }

    public String getUid() {
        return uid;
    }

    public Heartbeat setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getPortrait() {
        return portrait;
    }

    public Heartbeat setPortrait(String portrait) {
        this.portrait = portrait;
        return this;
    }
}
