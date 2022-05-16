package com.kinghis.yyoauth.dao;

import com.kinghis.yyoauth.common.WtxBaseMapper;
import com.kinghis.yyoauth.pojo.SysMenu;
import com.kinghis.yyoauth.pojo.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysMenuMapper extends WtxBaseMapper<SysMenu> {

    /**
     * 获取用户菜单权限
     * @param params
     * @return
     */
    List<SysMenu> getMenusByUser(Map<String, Object> params);

    /** 
    * @Description: 查询角色所拥有的菜单
    * @Author: sl 
    * @Date: 2019-03-01 9:38
    */
    List<SysMenu> queryListByRole(@Param("permission") SysRolePermission permission);
}