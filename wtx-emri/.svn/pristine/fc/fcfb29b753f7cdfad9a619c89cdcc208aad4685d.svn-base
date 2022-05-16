package com.kinghis.emri.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.emri.pojo.T_emri_source;

import java.sql.SQLException;
import java.util.List;

public interface SourceService {

    DataSet<T_emri_source> queryPage(T_emri_source request, Page page);

    T_emri_source getById(String source_code);

    void add(T_emri_source model);

    void delete(String ids);

    int testConnect(T_emri_source model) throws SQLException;

    List<T_emri_source> listSource();
}
