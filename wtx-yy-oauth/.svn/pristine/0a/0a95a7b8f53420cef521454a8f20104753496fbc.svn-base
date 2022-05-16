package com.kinghis.yyoauth.pojo;

import com.kinghis.common.model.WtxBasePojo;
import com.wtx.common.util.CommonUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "SYS_USER")
@Setter
@Getter
public class SysUser extends WtxBasePojo {
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "LOGIN_NAME")
    private String loginName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SEX_CODE")
    private String sexCode;

    @Column(name = "SEX_NAME")
    private String sexName;

    @Column(name = "ID_CARD")
    private String idCard;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "USER_TYPE")
    private String userType;

    @Column(name = "ORG_CODE")
    private String orgCode;

    @Column(name = "ORG_NAME")
    private String orgName;

    @Column(name = "IS_SUPER")
    private String isSuper;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "LAST_LOGIN_TIME")
    private Date lastLoginTime;

    @Column(name = "LAST_LOGIN_IP")
    private String lastLoginIp;

    /**
     * @Description: 统筹区编码
     * @Author: sl
     * @Date: 2019-02-28 11:04
     */
    @Column(name = "AAA027")
    private Long aaa027;

    //所属科室编码 多个以逗号隔开
    @Column(name = "DEPT_LIST")
    private String dept_list;

    //数据权限类型 1-全院 2-所属科室 3-个人 4-分院
    @Column(name = "DATA_AUTH")
    private String data_auth;


    @Column(name = "MEMBER_CODE")
    private String member_code;

    //所属院区 1-天心阁 2-马王堆 3-岳麓山
    @Column(name = "HOSP_LIST")
    private String hosp_list;


    public String getDeptList(){
        if (CommonUtil.isNotEmpty(this.dept_list)){
            String[] arr = this.dept_list.split(",");
            String str = "";
            for (String s : arr){
                str += "'" + s + "',";
            }
            return str.substring(0, str.length() - 1);
        }
        return "";
    }

    public String getHospList(){
        if (CommonUtil.isNotEmpty(this.hosp_list)){
            String[] arr = this.hosp_list.split(",");
            String str = "";
            for (String s : arr){
                str += "'" + s + "',";
            }
            return str.substring(0, str.length() - 1);
        }
        return "";
    }



}