package com.mjoys.po;


import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "t_wx_task", catalog = "voice_robot")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "business_id")
    private int businessId;

    private Integer operate;

    @Column(name = "wx_account_id")
    private String wxAccountId;

    @Column(name = "target_account")
    private String targerAccount;

    private String message;

    @Column(name = "action_submit_status")
    private Integer actionSubmitStatus;

    @Column(name = "action_execution_status")
    private Integer actionExecutionStatus;

    @Column(name = "action_result_status")
    private Integer actionResultStatus;

    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_create", insertable = true, updatable = false)
    private Date timeCreate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_modified", insertable = true, updatable = true)
    private Date timeModified = new Date();

    //动作下发状态：0-未提交 1-成功；2-失败
    public static final int ACTION_SUBMIT_STATUS_NOT_SUBMIT = 0;
    public static final int ACTION_SUBMIT_STATUS_SUCCESSED = 1;
    public static final int ACTION_SUBMIT_STATUS_FAILURE = 2;

    //动作执行状态：0-未知 1-成功；2-失败
    public static final int ACTION_EXECUTION_STATUS_UNKNOWN = 0;
    public static final int ACTION_EXECUTION_STATUS_SUCCESSED = 1;
    public static final int ACTION_EXECUTION_STATUS_FAILURE = 2;

    //动作执行结果：0-未知 1-添加成功；2-微信账户不存在；3-被限制;4-添加失败(拒绝)
    public static final int ACTION_RESULT_STATUS_UNKNOWN = 0;
    public static final int ACTION_RESULT_STATUS_SUCCESSED = 1;
    public static final int ACTION_RESULT_STATUS_WECHAT_ACCOUNT_NOT_EXISTS = 2;
    public static final int ACTION_RESULT_STATUS_LIMITED = 3;
    public static final int ACTION_RESULT_STATUS_REFUSED = 4;

}