package com.mjoys.protocol.message.report;

public class WechatAddFriendReport extends Report {
    //0-未知 1-添加成功；2-微信账户不存在；3-被限制;4-添加失败
    public static final int RESULT_UNKNOWN = 0;
    public static final int RESULT_ADD_SUCCESSED = 1;
    public static final int RESULT_WECHAT_ACCOUNT_IS_NOT_EXISTS = 2;
    public static final int RESULT_LIMITED = 3;
    public static final int RESULT_REFUSED = 4;

    private int commandId;
    private int result;

    public WechatAddFriendReport(int commandId, int result) {
        this.commandId = commandId;
        this.result = result;
    }

    public int getCommandId() {
        return commandId;
    }

    public WechatAddFriendReport setCommandId(int commandId) {
        this.commandId = commandId;
        return this;
    }

    public int getResult() {
        return result;
    }

    public WechatAddFriendReport setResult(int result) {
        this.result = result;
        return this;
    }
}
