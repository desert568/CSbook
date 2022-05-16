package com.kinghis.yyoauth.pojo;

import com.kinghis.common.model.WtxBasePojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "SYS_ROLE")
@Setter
@Getter
public class SysRole  extends WtxBasePojo {
    @Id
    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "ROLE_DESC")
    private String roleDesc;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "PROJECT_NAME")
    private String projectName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;
}