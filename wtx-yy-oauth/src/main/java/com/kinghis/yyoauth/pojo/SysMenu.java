package com.kinghis.yyoauth.pojo;

import com.kinghis.common.model.WtxBasePojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Table(name = "SYS_MENU")
@Setter
@Getter
public class SysMenu  extends WtxBasePojo {
    @Id
    @Column(name = "MENU_CODE")
    private String menuCode;

    @Column(name = "MENU_NAME")
    private String menuName;

    @Column(name = "PROJECT_CODE")
    private String projectCode;

    @Column(name = "PARENT_CODE")
    private String parentCode;

    @Column(name = "MENU_NO")
    private String menuNo;

    @Column(name = "MENU_URL")
    private String menuUrl;

    @Column(name = "ICON_CODE")
    private String iconCode;

    @Column(name = "ICON_URL")
    private String iconUrl;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    private List<String> childs;
}