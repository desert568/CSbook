package com.kinghis.yyoauth.pojo;

import com.kinghis.common.model.WtxBasePojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "SYS_LOGIN_LOG")
@Setter
@Getter
public class SysLoginLog  extends WtxBasePojo {
    @Id
    @Column(name = "LOG_ID")
    private String logId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "LOGIN_NAME")
    private String loginName;

    @Column(name = "LOGIN_TIME")
    private Date loginTime;

    @Column(name = "LOGIN_TYPE")
    private String loginType;

    @Column(name = "LOGIN_IP")
    private String loginIp;
}