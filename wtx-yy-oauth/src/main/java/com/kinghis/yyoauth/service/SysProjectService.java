package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.pojo.SysProject;

import java.util.List;

/**
 * @Desc 项目信息
 * @Date 2019-01-21
 * @Author liuc
 */
public interface SysProjectService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    SysProject getSysProject(String id);

    /**
     * 根据条件返回集合
     * @param sysProject
     * @return
     */
    List<SysProject> queryList(SysProject sysProject, String projectCode);

    /**
     * 根据条件分页查询
     * @param page
     * @param sysProject
     * @return
     */
    DataSet<SysProject> queryPage(SysProject sysProject, Page page, String projectCodes);


    /**
     * 保存
     * @param sysProject
     * @return
     */
    int save(SysProject sysProject);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 更新非空字段
     * @param sysProject
     * @return
     */
    int update(SysProject sysProject);

    /**
     * 更新所有字段，包括空和非空
     * @param sysProject
     * @return
     */
    int updateAll(SysProject sysProject);


    /**
     * @Description: 根据主键@Id进行删除，多个Id以逗号,分割
     * @Author: sl
     * @Date: 2019-01-23 14:08
     */
    int deleteByIds(String ids);

    /** @Desc 根据传进来的值查询对应的projectCode or projectName;
     * @author liub
     * @Date 2019/2/28 9:41
     */
    List<SysProject> listProjectSelect(String projectCode, String projectCodes);

    SysProject getSysProjectByCode(String sysCode);
}
