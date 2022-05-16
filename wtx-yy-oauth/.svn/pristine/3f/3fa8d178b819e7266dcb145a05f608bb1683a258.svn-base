package com.kinghis.yyoauth.model;

import com.kinghis.common.model.WtxBasePojo;
import com.kinghis.yyoauth.pojo.*;

import java.util.List;
import java.util.Map;

/**
 * @Desc 用户权限对象
 * @Date 2019/1/23 17:21
 * @Author liuc
 */
public class QxUser extends WtxBasePojo {

    private SysUser sysUser;


    //如果是超级管理员，那么这里是管理的项目列表
    private List<SysProject> projects;
    private Map<String,Map<String,SysFunc>> funcs;//外面map的key为菜单编码，里面map的key为资源编码
    private Map<String,SysRole> roles;
    private Map<String,SysMenu> menus;//key为菜单编码

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public List<SysProject> getProjects() {
        return projects;
    }

    public void setProjects(List<SysProject> projects) {
        this.projects = projects;
    }

    public Map<String, Map<String, SysFunc>> getFuncs() {
        return funcs;
    }

    public void setFuncs(Map<String, Map<String, SysFunc>> funcs) {
        this.funcs = funcs;
    }

    public Map<String, SysRole> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, SysRole> roles) {
        this.roles = roles;
    }

    public Map<String, SysMenu> getMenus() {
        return menus;
    }

    public void setMenus(Map<String, SysMenu> menus) {
        this.menus = menus;
    }
}
