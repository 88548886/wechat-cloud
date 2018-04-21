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
@Table(name = "t_wx_basic_config", catalog = "voice_robot")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "wx_account_id")
    private String wechatAccountId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "business_id")
    private String bussinessId;

    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_create", insertable = true, updatable = false)
    private Date timeCreate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_modified", insertable = true, updatable = true)
    private Date timeModified = new Date();
}
