package com.kinghis.yyoauth.listener;

import com.alibaba.fastjson.JSON;
import com.kinghis.common.cache.RedisService;
import com.kinghis.common.constants.LoginConstants;
import com.kinghis.common.model.LoginOutModel;
import com.kinghis.common.util.SSOHttpUtil;
import com.kinghis.common.util.SpringUtil;
import com.kinghis.yyoauth.common.config.OauthWebConfig;
import com.wtx.common.util.CommonUtil;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2019-01-30 14:08
 */
@WebListener
public class SessionManager implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        //设置session超时时长
        httpSessionEvent.getSession().setMaxInactiveInterval(OauthWebConfig.loginuserTimeout.intValue());
    }
    
    /** 
    * @Description: session销毁时调用
    * @Author: sl 
    * @Date: 2019-01-30 14:26
    */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //当服务端session超时后，发送请求退出所有登录过的子系统
        HttpSession session = httpSessionEvent.getSession();
        String ticket = (String) session.getAttribute(LoginConstants.SSO_TICKET);
        if (CommonUtil.isNotEmpty(ticket)){
            RedisService redisService = SpringUtil.getBean(RedisService.class);
            redisService.deleteKey(ticket);
            //退出所有子系统
            String loginOutStr = (String) redisService.get(ticket + LoginConstants.SSO_LOGIN_OUT);
            List<LoginOutModel> list = null;
            if (CommonUtil.isNotEmpty(loginOutStr)){
                list = JSON.parseArray(loginOutStr, LoginOutModel.class);
            } else {
                list = new ArrayList<>();
            }
            for (LoginOutModel outModel : list){
                try {
                    SSOHttpUtil.sendHttpRequest(outModel.getOut_url(), outModel.getJsessionid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //删除服务端session
            session.invalidate();
            redisService.deleteKey(ticket + LoginConstants.SSO_LOGIN_OUT);
        }
    }
}
