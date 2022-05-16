package com.kinghis.yyoauth.model.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2021-12-01 16:58
 */
@Data
public class UserRoleModel implements Serializable {

    private String roleCode;

    private String roleName;

    private String sysCode;

    private String userId;

    private String userCode; //用户编码

    private String isDeleted; //删除标记-Y删除N否（N就是新增的时候发送）


    private String cardno;

    private String deptCode;

    private String mobileNo;

    private String status; //用户状态，1启用，0禁用

    private String userName;

    private String password;
}
