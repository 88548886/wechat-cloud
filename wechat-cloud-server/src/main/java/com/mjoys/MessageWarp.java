package com.mjoys;


import com.mjoys.protocol.Message;

public class MessageWarp {
    private Message msg;
    private Terminal.Addr terminalAddr;

    public Message getMsg() {
        return msg;
    }

    public MessageWarp setMsg(Message msg) {
        this.msg = msg;
        return this;
    }


    public Terminal.Addr getTerminalAddr() {
        return terminalAddr;
    }

    public MessageWarp setTerminalAddr(Terminal.Addr terminalAddr) {
        this.terminalAddr = terminalAddr;
        return this;
    }
}
