package com.kinghis.yyoauth.pojo;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @Description: : 科室管理
 * @Author: yzm
 * @Date: 2020/3/24 14:57
 */
@Data
@Table(name = "T_JXKH_DEPT")
public class T_jxkh_dept {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "ORG_CODE")
    private String org_code;

    @Column(name = "DEPT_CODE")
    private String dept_code;//科室编码

    @Column(name = "DEPT_NAME")
    private String dept_name;//科室名称

    @Column(name = "ISFLAG")
    private String isflag;//是否填报科室

    @Column(name = "ORG_NAME")
    private String org_name;
}
