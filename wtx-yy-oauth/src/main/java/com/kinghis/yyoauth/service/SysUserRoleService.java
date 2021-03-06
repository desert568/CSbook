package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.model.api.UserRoleModel;
import com.kinghis.yyoauth.pojo.SysUserRole;

import java.util.List;

/**
 * @Desc 用户角色信息
 * @Author liuc
 * @Date 2019-01-29 12:24
 */
public interface SysUserRoleService {

    /**
     * @Desc 根据主键查询
     * @Author liuc
     * @Date 2019-01-29 12:24
     */
    SysUserRole getSysUserRole(String id);

    /**
     * @Desc 根据条件返回集合
     * @Author liuc
     * @Date 2019-01-29 12:24
     */
    List<SysUserRole> queryList(SysUserRole sysUserRole);

    /**
     * @Desc 根据条件分页查询
     * @Author liuc
     * @Date 2019-01-29 12:24
     */
    DataSet<SysUserRole> queryPage(SysUserRole sysUserRole, Page page);


    /**
     * @Desc 保存
     * @Author liuc
     * @Date 2019-01-29 12:24
     */
    int save(SysUserRole sysUserRole);

    /**
     * @Desc 根据主键删除
     * @Author liuc
     * @Date 2019-01-29 12:24
     */
    int delete(String id);

    /**
     * @Desc 更新非空字段
     * @Author liuc
     * @Date 2019-01-29 12:24
     */
    int update(SysUserRole sysUserRole);

    /**
     * @Desc 更新所有字段，包括空和非空
     * @Author liuc
     * @Date 2019-01-29 12:24
     */
    int updateAll(SysUserRole sysUserRole);


    void batchSave(List<SysUserRole> list);

    void saveApiUserRole(UserRoleModel userRole);
}
