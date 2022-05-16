package com.kinghis.emri.service;

import com.kinghis.emri.pojo.T_emri_sql;

import java.util.List;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-10 17:29
 */
public interface SQLService {

    T_emri_sql queryByTableName(String tableName);

    void add(T_emri_sql model);

    List<T_emri_sql> listTable();

    List<T_emri_sql> queryEmrSqlList(T_emri_sql emriSql);

    /**
     * @DESC: //TODO 操作sql
     * @Author: liubo
     * @Date: 2020/9/16 4:58 下午
     */
    int operSql(String sql);

    /**
     * @DESC: //TODO 查询pat_interface表条数
     * @Author: liubo
     * @Date: 2020/9/21 8:59 上午
     */
    int queryPatInterfaceCount(String patient_id, String visit_id);

}
