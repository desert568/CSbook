package com.kinghis.yyoauth.service;

import com.kinghis.common.model.LoginToken;
import com.kinghis.yyoauth.model.QxUser;
import com.kinghis.yyoauth.pojo.SysUser;

/**
 * @Desc
 * @Date 2019/1/23 17:24
 * @Author liuc
 */
public interface LoginService {

    /**
     * 用户登录
     * @param
     * @return
     */
    QxUser getQxUser(String userId, String projectCode);

    SysUser ssoLogin(LoginToken token);
}
