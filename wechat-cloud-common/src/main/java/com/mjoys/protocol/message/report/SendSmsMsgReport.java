package com.mjoys.protocol.message.report;



public class SendSmsMsgReport extends Report {
    public static final int RESULT_SEND_SUCCESSED = 0;
    public static final int RESULT_SEND_FAILURE = 1;
    public int commandId;
    public int result;

    public SendSmsMsgReport(int commandId, int result) {
        this.commandId = commandId;
        this.result = result;
    }

    public int getCommandId() {
        return commandId;
    }

    public SendSmsMsgReport setCommandId(int commandId) {
        this.commandId = commandId;
        return this;
    }

    public int getResult() {
        return result;
    }

    public SendSmsMsgReport setResult(int result) {
        this.result = result;
        return this;
    }
}
