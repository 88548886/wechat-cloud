package com.mjoys.protocol.message.command;


public class WechatAddFriendCommand extends Command {
    private int commandId;
    private String wechatAccount;
    private String message;

    public WechatAddFriendCommand(int commandId, String wechatAccount, String message) {
        this.commandId = commandId;
        this.wechatAccount = wechatAccount;
        this.message = message;
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
