package com.kinghis.yyoauth.pojo;

import com.kinghis.common.model.WtxBasePojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SYS_USER_PROJECT")
@Setter
@Getter
public class SysUserProject  extends WtxBasePojo {
    @Id
    @Column(name = "U_ID")
    private String uId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "PROJECT_CODE")
    private String projectCode;
}