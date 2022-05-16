package com.kinghis.yyoauth.pojo;

import com.kinghis.common.model.WtxBasePojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SYS_FUNC")
@Setter
@Getter
public class SysFunc extends WtxBasePojo{
    @Id
    @Column(name = "FUNC_CODE")
    private String funcCode;

    @Column(name = "FUNC_NAME")
    private String funcName;

    @Column(name = "MENU_CODE")
    private String menuCode;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "FUNC_DESC")
    private String funcDesc;
}