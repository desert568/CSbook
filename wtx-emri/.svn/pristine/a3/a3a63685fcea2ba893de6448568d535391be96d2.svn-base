package com.kinghis.emri.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-10 17:32
 */
@Table(name = "T_emri_sql")
@Data
public class T_emri_sql implements Serializable {

    @Id
    private String table_name;

    private String query_sql;

    private String delete_sql;

    private String insert_sql;

    private String source_code;

    private String table_desc;

}
