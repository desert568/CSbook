package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.pojo.SysMenu;
import com.kinghis.yyoauth.pojo.SysRolePermission;

import java.util.List;

/**
 * @Desc 菜单信息
 * @Date 2019-01-21
 * @Author liuc
 */
public interface SysMenuService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    SysMenu getSysMenu(String id);

    /**
     * 根据条件返回集合
     * @param sysMenu
     * @return
     */
    List<SysMenu> queryList(SysMenu sysMenu);

    /**
     * 根据条件分页查询
     * @param page
     * @param sysMenu
     * @return
     */
    DataSet<SysMenu> queryPage(SysMenu sysMenu, Page page);


    /**
     * 保存
     * @param sysMenu
     * @return
     */
    int save(SysMenu sysMenu);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 更新非空字段
     * @param sysMenu
     * @return
     */
    int update(SysMenu sysMenu);

    /**
     * 更新所有字段，包括空和非空
     * @param sysMenu
     * @return
     */
    int updateAll(SysMenu sysMenu);


    List<SysMenu> queryListByRole(SysRolePermission permission);
}
