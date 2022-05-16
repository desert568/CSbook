package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.pojo.SysLoginLog;

import java.util.List;

/**
 * @Desc 登录日志
 * @Date 2019-01-21
 * @Author liuc
 */
public interface SysLoginLogService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    SysLoginLog getSysLoginLog(String id);

    /**
     * 根据条件返回集合
     * @param sysLoginLog
     * @return
     */
    List<SysLoginLog> queryList(SysLoginLog sysLoginLog);

    /**
     * 根据条件分页查询
     * @param page
     * @param sysLoginLog
     * @return
     */
    DataSet<SysLoginLog> queryPage(SysLoginLog sysLoginLog, Page page);


    /**
     * 保存
     * @param sysLoginLog
     * @return
     */
    int save(SysLoginLog sysLoginLog);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 更新非空字段
     * @param sysLoginLog
     * @return
     */
    int update(SysLoginLog sysLoginLog);

    /**
     * 更新所有字段，包括空和非空
     * @param sysLoginLog
     * @return
     */
    int updateAll(SysLoginLog sysLoginLog);


}
