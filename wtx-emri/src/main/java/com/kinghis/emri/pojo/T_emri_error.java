package com.kinghis.emri.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @DESC: //TODO 错误信息表
 * @Author: liubo
 * @Date: 2020/9/16 7:45 下午
 */
@Table(name = "T_emri_error")
@Data
public class T_emri_error implements Serializable {

    @Id
    private String id;

    private String patient_id;

    private String visit_id;

    private String start_date;

    private String end_date;

    private String oper;

    private String oper_date;

    private String source;

    private String error_msg;


}
