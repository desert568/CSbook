package com.kinghis.emri.dao;

import com.kinghis.common.mapper.WtxBaseMapper;
import com.kinghis.emri.pojo.T_emri_sql;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-10 17:30
 */
public interface SQLMapper extends WtxBaseMapper<T_emri_sql> {

    List<T_emri_sql> listTable();

    /**
     * @DESC: //TODO 根据传入sql 执行语句
     * @Author: liubo
     * @Date: 2020/9/16 4:57 下午
     */
    int operSql(@Param("sql") String sql);

    int queryPatInterfaceCount(@Param("tableName") String tableName,
                               @Param("patient_id") String patient_id,
                               @Param("visit_id") String visit_id);
}
