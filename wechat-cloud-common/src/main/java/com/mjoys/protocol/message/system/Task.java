package com.mjoys.protocol.message.system;

import com.mjoys.protocol.MessageBody;

public class Task extends MessageBody {
    /**
     * 标识一个任务
     */
    private int id;
    /**
     * 标识一个客户
     */
    private int userId;
    /**
     * 标识一个业务
     */
    private int bussinessId;
    /**
     * 标识一个分组
     */
    private int groupId;
    /**
     * 标识一种操作
     */
    private int commandType;
    /**
     * 标识一个执行的终端
     */
    private String terminalUid;
    /**
     * 标识一个执行的终端在缓存的hash
     */
    private String terminalAddr;
    /**
     * 标识一个被执行者
     */
    private String receiver;
    /**
     * 标识一个附加信息
     */
    private String message;

    public int getId() {
        return id;
    }

    public Task setId(int id) {
        this.id = id;
        return this;
    }



    public int getBussinessId() {
        return bussinessId;
    }

    public Task setBussinessId(int bussinessId) {
        this.bussinessId = bussinessId;
        return this;
    }

    public int getGroupId() {
        return groupId;
    }

    public Task setGroupId(int groupId) {
        this.groupId = groupId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Task setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getCommandType() {
        return commandType;
    }

    public Task setCommandType(int commandType) {
        this.commandType = commandType;
        return this;
    }

    public String getTerminalUid() {
        return terminalUid;
    }

    public Task setTerminalUid(String terminalUid) {
        this.terminalUid = terminalUid;
        return this;
    }

    public String getTerminalAddr() {
        return terminalAddr;
    }

    public Task setTerminalAddr(String terminalAddr) {
        this.terminalAddr = terminalAddr;
        return this;
    }

    public String getReceiver() {
        return receiver;
    }

    public Task setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Task setMessage(String message) {
        this.message = message;
        return this;
    }
}
