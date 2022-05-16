package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.pojo.SysUserProject;

import java.util.List;

/**
 * @Desc 用户项目权限信息
 * @Date 2019-01-21
 * @Author liuc
 */
public interface SysUserProjectService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    SysUserProject getSysUserProject(String id);

    /**
     * 根据条件返回集合
     * @param sysUserProject
     * @return
     */
    List<SysUserProject> queryList(SysUserProject sysUserProject);

    /**
     * 根据条件分页查询
     * @param page
     * @param sysUserProject
     * @return
     */
    DataSet<SysUserProject> queryPage(SysUserProject sysUserProject, Page page);


    /**
     * 保存
     * @param sysUserProject
     * @return
     */
    int save(SysUserProject sysUserProject);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 更新非空字段
     * @param sysUserProject
     * @return
     */
    int update(SysUserProject sysUserProject);

    /**
     * 更新所有字段，包括空和非空
     * @param sysUserProject
     * @return
     */
    int updateAll(SysUserProject sysUserProject);


}
