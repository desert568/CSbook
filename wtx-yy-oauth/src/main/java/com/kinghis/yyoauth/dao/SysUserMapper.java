package com.kinghis.yyoauth.dao;


import com.kinghis.yyoauth.common.WtxBaseMapper;
import com.kinghis.common.model.LoginToken;
import com.kinghis.yyoauth.pojo.SysUser;
import org.apache.ibatis.annotations.Param;


public interface SysUserMapper extends WtxBaseMapper<SysUser> {

    /**
      *@Desc 用户登录
      *@Author liuc
      *@Date 2019/1/29 8:56
      */
    SysUser login(@Param("loginToken") LoginToken loginToken);


    SysUser selectList(String userId);
}