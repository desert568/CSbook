package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.model.api.DeptModel;
import com.kinghis.yyoauth.model.api.MemberModel;
import com.kinghis.yyoauth.model.api.PasswordModel;
import com.kinghis.yyoauth.pojo.SysUser;

import java.util.List;

/**
 * @Desc 用户信息
 * @Author liuc
 * @Date 2019/1/28 18:05
 */
public interface SysUserService {

    /**
      *@Desc 根据主键查询
      *@Author liuc
      *@Date 2019/1/29 8:45
      */
    SysUser getSysUser(String id);

    /**
      *@Desc 根据条件返回集合
      *@Author liuc
      *@Date 2019/1/29 8:46
      */
    List<SysUser> queryList(SysUser sysUser);

    /**
      *@Desc 根据条件分页查询
      *@Author liuc
      *@Date 2019/1/29 8:46
      */
    DataSet<SysUser> queryPage(SysUser sysUser, Page page);


    /**
      *@Desc 保存
      *@Author liuc
      *@Date 2019/1/29 8:46
      */
    int save(SysUser sysUser);

    /**
      *@Desc 根据主键删除
      *@Author liuc
      *@Date 2019/1/29 8:46
      */
    int delete(String id);

    /**
      *@Desc 更新非空字段
      *@Author liuc
      *@Date 2019/1/29 8:46
      */
    int update(SysUser sysUser);

    /**
      *@Desc 更新所有字段，包括空和非空
      *@Author liuc
      *@Date 2019/1/29 8:47
      */
    int updateAll(SysUser sysUser);

    /**
      *@Desc 根据主键@Id进行删除，多个Id以逗号,分割
      *@Author sl
      *@Date 2019/1/29 8:47
      */
    int deleteByIds(String ids);

    /**
     *@Desc 修改密码
     *@Author yzm
     *@Date 2019/2/12 11:47
     */
     void upadatePassWord(SysUser user);


    void updateApiUser(MemberModel member);

    void updateApiPassword(PasswordModel password);
}
