package com.kinghis.emri.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.emri.pojo.T_emri_error;

/**
 * @DESC: //TODO
 * @Author: liubo
 * @Date: 2020/9/16 7:45 下午
 */
public interface ErrorService {

    /**
     * @DESC: //TODO 新增
     * @Author: liubo
     * @Date: 2020/9/16 7:45 下午
     */
    void add(T_emri_error error);

    DataSet<T_emri_error> queryError(T_emri_error request, Page page);

    T_emri_error getById(String id);

    /**
     * @Description: 清空错误日志
     * @Author: sl
     * @Date: 2020-11-23 10:58
     */
    void deleteAll(T_emri_error request);
}
