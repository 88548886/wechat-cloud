package com.mjoys.protocol.message.business;


import com.mjoys.protocol.MessageBody;

public class WehcatAddFriendRequest extends MessageBody {
    private int taskId;
    private String target;
    private String message;

    public WehcatAddFriendRequest(int taskId, String target, String message) {
        this.taskId = taskId;
        this.target = target;
        this.message = message;
    }

    public int getTaskId() {
        return taskId;
    }

    public WehcatAddFriendRequest setTaskId(int taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public WehcatAddFriendRequest setTarget(String target) {
        this.target = target;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public WehcatAddFriendRequest setMessage(String message) {
        this.message = message;
        return this;
    }
}
