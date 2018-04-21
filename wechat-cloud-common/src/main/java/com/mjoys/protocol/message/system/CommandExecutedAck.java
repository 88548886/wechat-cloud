package com.mjoys.protocol.message.system;

/**
 * 执行command之后返回确认
 */
public class CommandExecutedAck {
    private int commandId;
    private String terminalUid;

    public int getCommandId() {
        return commandId;
    }

    public CommandExecutedAck setCommandId(int commandId) {
        this.commandId = commandId;
        return this;
    }

    public String getTerminalUid() {
        return terminalUid;
    }

    public CommandExecutedAck setTerminalUid(String terminalUid) {
        this.terminalUid = terminalUid;
        return this;
    }
}
