package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.pojo.SysRolePermission;

import java.util.List;

/**
 * @Desc 角色权限信息
 * @Date 2019-01-28
 * @Author liuc
 */
public interface SysRolePermissionService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    SysRolePermission getSysRolePermission(String id);

    /**
     * 根据条件返回集合
     * @param sysRolePerm
     * @return
     */
    List<SysRolePermission> queryList(SysRolePermission sysRolePerm);

    /**
     * 根据条件分页查询
     * @param page
     * @param sysRolePerm
     * @return
     */
    DataSet<SysRolePermission> queryPage(SysRolePermission sysRolePerm, Page page);


    /**
     * 保存
     * @param sysRolePerm
     * @return
     */
    int save(SysRolePermission sysRolePerm);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 更新非空字段
     * @param sysRolePerm
     * @return
     */
    int update(SysRolePermission sysRolePerm);

    /**
     * 更新所有字段，包括空和非空
     * @param sysRolePerm
     * @return
     */
    int updateAll(SysRolePermission sysRolePerm);


    void batchSave(List<SysRolePermission> list);
}
