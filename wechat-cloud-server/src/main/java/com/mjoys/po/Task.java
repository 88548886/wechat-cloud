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

    private Integer operate;

    @Column(name = "targer_account")
    private String targerAccount;

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
}
