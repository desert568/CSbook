package com.kinghis.yyoauth.pojo;

import com.kinghis.common.model.WtxBasePojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SYS_ROLE_PERMISSION")
@Setter
@Getter
public class SysRolePermission  extends WtxBasePojo {
    @Id
    @Column(name = "U_ID")
    private String uId;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "MENU_CODE")
    private String menuCode;

    @Column(name = "FUNC_CODE")
    private String funcCode;

    @Column(name = "PROJECT_CODE")
    private String projectCode;
}