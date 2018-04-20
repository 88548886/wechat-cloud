package com.mjoys.protocol.message.business;

import com.mjoys.protocol.MessageBody;

public class TaskRequest extends MessageBody{
    private int id;
    private int uid;
    private int groupId;
    private int operationCode;
    private String processor;
    private int channelHashCode;
    private String target;
    private String message;

    public int getId() {
        return id;
    }

    public TaskRequest setId(int id) {
        this.id = id;
        return this;
    }

    public int getUid() {
        return uid;
    }

    public TaskRequest setUid(int uid) {
        this.uid = uid;
        return this;
    }

    public int getGroupId() {
        return groupId;
    }

    public TaskRequest setGroupId(int groupId) {
        this.groupId = groupId;
        return this;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public TaskRequest setOperationCode(int operationCode) {
        this.operationCode = operationCode;
        return this;
    }

    public String getProcessor() {
        return processor;
    }

    public TaskRequest setProcessor(String processor) {
        this.processor = processor;
        return this;
    }

    public int getChannelHashCode() {
        return channelHashCode;
    }

    public TaskRequest setChannelHashCode(int channelHashCode) {
        this.channelHashCode = channelHashCode;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public TaskRequest setTarget(String target) {
        this.target = target;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public TaskRequest setMessage(String message) {
        this.message = message;
        return this;
    }
}
