package com.mjoys.protocol.message.command;


public class WechatAddFriendCommand extends Command {
    private int commandId;
    private long executeTime;
    private String wechatAccount;
    private String message;

    public WechatAddFriendCommand(int commandId, long executeTime, String wechatAccount, String message) {
        this.commandId = commandId;
        this.executeTime = executeTime;
        this.wechatAccount = wechatAccount;
        this.message = message;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public WechatAddFriendCommand setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
        return this;
    }

    public int getCommandId() {
        return commandId;
    }

    public WechatAddFriendCommand setCommandId(int commandId) {
        this.commandId = commandId;
        return this;
    }

    public String getWechatAccount() {
        return wechatAccount;
    }

    public WechatAddFriendCommand setWechatAccount(String wechatAccount) {
        this.wechatAccount = wechatAccount;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public WechatAddFriendCommand setMessage(String message) {
        this.message = message;
        return this;
    }
}
