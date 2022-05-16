package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.pojo.SysFunc;

import java.util.List;

/**
 * @Desc 资源信息
 * @Date 2019-01-21
 * @Author liuc
 */
public interface SysFuncService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    SysFunc getSysFunc(String id);

    /**
     * 根据条件返回集合
     * @param sysFunc
     * @return
     */
    List<SysFunc> queryList(SysFunc sysFunc);

    /**
     * 根据条件分页查询
     * @param page
     * @param sysFunc
     * @return
     */
    DataSet<SysFunc> queryPage(SysFunc sysFunc, Page page);


    /**
     * 保存
     * @param sysFunc
     * @return
     */
    int save(SysFunc sysFunc);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 更新非空字段
     * @param sysFunc
     * @return
     */
    int update(SysFunc sysFunc);

    /**
     * 更新所有字段，包括空和非空
     * @param sysFunc
     * @return
     */
    int updateAll(SysFunc sysFunc);

    /**
     * @Description: 根据funcCode编码查询SysFunc
     * @Author: sl
     * @Date: 2019-02-14 15:04
     */
    List<SysFunc> queryListInCodes(SysFunc sysFunc, String funcCodes);
}
