package com.kinghis.yyoauth.dao;

import com.kinghis.yyoauth.common.WtxBaseMapper;
import com.kinghis.yyoauth.pojo.SysProject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysProjectMapper extends WtxBaseMapper<SysProject> {
    
    /** 
    * @Description: 通过用户ID查询所拥有权限的项目列表
    * @Author: sl 
    * @Date: 2019-03-01 9:39
    */
    List<SysProject> getProjectByUser(String userId);
    
    /** 
    * @Description: 通过项目编码查询项目
    * @Author: sl
    * @Date: 2019-03-01 9:41
    */
    List<SysProject> listProjectSelect(@Param("projectCode") String projectCode, @Param("projectCodeList") List<String> projectCodeList);
}