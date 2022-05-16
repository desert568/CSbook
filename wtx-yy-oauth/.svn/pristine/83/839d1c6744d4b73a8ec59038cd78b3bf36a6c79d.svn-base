package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.pojo.SysRole;

import java.util.List;

/**
 * @Desc 角色信息
 * @Date 2019-01-21
 * @Author liuc
 */
public interface SysRoleService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    SysRole getSysRole(String id);

    /**
     * 根据条件返回集合
     * @param sysRole
     * @return
     */
    List<SysRole> queryList(SysRole sysRole, String projectCodes);

    /**
     * 根据条件分页查询
     * @param page
     * @param sysRole
     * @return
     */
    DataSet<SysRole> queryPage(SysRole sysRole, Page page, String projectCodes);


    /**
     * 保存
     * @param sysRole
     * @return
     */
    int save(SysRole sysRole);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 更新非空字段
     * @param sysRole
     * @return
     */
    int update(SysRole sysRole);

    /**
     * 更新所有字段，包括空和非空
     * @param sysRole
     * @return
     */
    int updateAll(SysRole sysRole);


    void deleteByIds(String ids);
}
