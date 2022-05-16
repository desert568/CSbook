package com.kinghis.yyoauth.model.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @DESC;    //
 * @Author;    // sl
 * @Date;    // 2021-11-04 17;    //26
 */
@Data
public class DeptModel implements Serializable {

    private String id;    //主键ID,
    private String corpCode;    //机构,
    private String deptCode;    //部门编码,
    private String deptName;    //科室名称,
    private String pdCode;    //上级编码,
    private String deptLevel;    //科室层级,
    private String orgFlag;    //机构类型;    //0为集团,1为公司(院区),2为部门,
    private String hospCode;    //院区编码,
    private String hospName;    //院区名称
}
