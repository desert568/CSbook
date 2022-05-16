package com.kinghis.emri.dao;

import com.kinghis.common.mapper.WtxBaseMapper;
import com.kinghis.emri.pojo.T_emri_error;

import java.util.List;

/**
 * @DESC: //TODO
 * @Author: liubo
 * @Date: 2020/9/16 7:42 下午
 */
public interface ErrorMapper extends WtxBaseMapper<T_emri_error> {

    List<T_emri_error> queryList(T_emri_error request);


    void deleteAll(T_emri_error request);
}
