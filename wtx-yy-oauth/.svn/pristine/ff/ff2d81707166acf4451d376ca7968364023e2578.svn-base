package com.kinghis.yyoauth.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.common.WtxBaseMapper;
import com.kinghis.yyoauth.pojo.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SysRoleMapper extends WtxBaseMapper<SysRole> {

    /**
    * @Description: 通过项目编码集合查询角色列表
    * @Author: sl
    * @Date: 2019-03-01 9:38
    */
    List<SysRole> queryList(@Param("sysRole") SysRole sysRole, @Param("projectCodeList") List<String> projectCodeList);

    /**
     * @Description: 通过项目编码集合分页查询角色列表
     * @Author: sl
     * @Date: 2019-03-01 9:38
     */
    List<SysRole> queryPage(Page page, @Param("sysRole") SysRole sysRole, @Param("projectCodeList") List<String> projectCodeList);

    List<SysRole> queryRoleList(@Param("userid") String userid, @Param("projectCode") String projectCode);
}