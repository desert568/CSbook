package com.kinghis.emri.dao;

import com.kinghis.common.mapper.WtxBaseMapper;
import com.kinghis.emri.pojo.T_emri_source;

import java.util.List;

public interface SourceMapper extends WtxBaseMapper<T_emri_source> {

    List<T_emri_source> queryList(T_emri_source request);
}
