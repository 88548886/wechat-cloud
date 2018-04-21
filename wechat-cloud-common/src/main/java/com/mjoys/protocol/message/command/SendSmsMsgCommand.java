package com.mjoys.protocol.message.command;


public class SendSmsMsgCommand extends Command {

    public int commandId;
    private long executeTime;
    private String receiver;
    private String message;

    public SendSmsMsgCommand(int commandId, long executeTime, String receiver, String message) {
        this.commandId = commandId;
        this.executeTime = executeTime;
        this.receiver = receiver;
        this.message = message;
    }

    public int getCommandId() {
        return commandId;
    }

    public SendSmsMsgCommand setCommandId(int commandId) {
        this.commandId = commandId;
        return this;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public SendSmsMsgCommand setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
        return this;
    }

    public String getReceiver() {
        return receiver;
    }

    public SendSmsMsgCommand setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SendSmsMsgCommand setMessage(String message) {
        this.message = message;
        return this;
    }
}
