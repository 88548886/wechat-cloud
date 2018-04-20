package com.mjoys.protocol.message.system;

/**
 * 收到command之后返回确认
 */
public class CommandReceivedAck {
    private int commandId;
    private int terminalUid;

    public int getCommandId() {
        return commandId;
    }

    public CommandReceivedAck setCommandId(int commandId) {
        this.commandId = commandId;
        return this;
    }

    public int getTerminalUid() {
        return terminalUid;
    }

    public CommandReceivedAck setTerminalUid(int terminalUid) {
        this.terminalUid = terminalUid;
        return this;
    }
}
