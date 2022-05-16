package com.kinghis.emri.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Description: 数据源连接表
 * @Author: sl
 * @Date: 2019-10-08 14:50
 */
@Table(name = "T_emri_SOURCE")
@Data
public class T_emri_source implements java.io.Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 4561611997191415228L;

    /**
     * 编码
     */
    @Id
    private String source_code;

    /**
     * 数据库类型
     */
    private String source_type;

    /**
     * 数据库名称
     */
    private String source_name;

    /**
     * 数据库连接url
     */
    private String source_url;

    /**
     * 数据库用户名
     */
    private String user_name;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 修改时间
     */
    private Date update_time;
}