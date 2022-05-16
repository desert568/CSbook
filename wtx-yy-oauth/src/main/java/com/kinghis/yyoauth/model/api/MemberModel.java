package com.kinghis.yyoauth.model.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @DESC;    //
 * @Author;    // sl
 * @Date;    // 2021-11-04 17;
 */
@Data
public class MemberModel implements Serializable {

    private String id;
    private String corpCode;    //机构编码,
    private String userCode;    //用户编码,
    private String oldUserCode;    //旧编码,
    private String userName;    //用户名称,
    private String deptCode;    //科室代码,
    private String password;    //密码,
    private String oldPassword;    //旧密码,
    private String status;    //状态,--0禁用，1启用
    private String mobileNo;    //手机号,
    private String dutyCode;    //职务代码
}
