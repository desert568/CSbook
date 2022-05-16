package com.kinghis.emri.service.impl;

import com.kinghis.emri.config.PropertiesConfig;
import com.kinghis.emri.dao.SQLMapper;
import com.kinghis.emri.pojo.T_emri_sql;
import com.kinghis.emri.service.SQLService;
import com.kinghis.emri.util.FileUtil;
import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-10 17:29
 */
@Service
public class SQLServiceImpl implements SQLService {

    @Resource
    private SQLMapper sqlMapper;

    @Value("${base_table_name}")
    private String tableName;

    @Override
    public T_emri_sql queryByTableName(String tableName) {
        T_emri_sql sql = sqlMapper.selectByPrimaryKey(tableName);
        return sql;
    }

    @Override
    public void add(T_emri_sql model) {
        if (CommonUtil.isEmpty(model.getTable_name())) {
            throw new CommonException("接口表不能为空");
        }
        //先删除，后重新插入
        sqlMapper.deleteByPrimaryKey(model.getTable_name());

        sqlMapper.insertSelective(model);
    }

    @Override
    public List<T_emri_sql> listTable() {
        return sqlMapper.listTable();
    }

    @Override
    public List<T_emri_sql> queryEmrSqlList(T_emri_sql emriSql) {
        return sqlMapper.select(emriSql);
    }

    @Override
    public int operSql(String sql) {
        return sqlMapper.operSql(sql);
    }

    @Override
    public int queryPatInterfaceCount(String patient_id, String visit_id) {
        if (CommonUtil.isEmpty(tableName)) {
            tableName = "pat_interface";
        }
        return sqlMapper.queryPatInterfaceCount(tableName, patient_id, visit_id);
    }

}
