package com.kinghis.yyoauth.model;

import cn.trasen.commons.util.CookieUtils;
import com.kinghis.common.bean.BaseContext;
import com.kinghis.common.bean.GuestSession;
import com.kinghis.common.cache.RedisService;
import com.kinghis.common.constants.LoginConstants;
import com.kinghis.common.util.SpringUtil;
import com.kinghis.yyoauth.pojo.SysProject;
import com.kinghis.yyoauth.pojo.SysUser;
import com.wtx.common.util.CommonUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Desc 会话信息获取
 * @Date 2019/1/27 13:31
 * @Author liuc
 */
public class WtxUserInfo {
    
    /**
    * @Desc: 获取用户权限信息
    * @Author: sl 
    * @Date: 2019-02-18 11:36
    */
    public static QxUser getQxUser(){
        RedisService redisService = SpringUtil.getBean(RedisService.class);
        //String token = GuestSession.getCookie();
        String token = getCookie() + LoginConstants.PROJECT_CODE;
        String redisKey = getQxUserRedisKey(token);
        QxUser qxUser = (QxUser)redisService.get(redisKey);
        return qxUser;
    }

    public static String getCookie(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        String token = CookieUtils.getCookie(request, "wtx_cookie_id");
        if (CommonUtil.isEmpty(token)) {
            token = CommonUtil.generateId();
            CookieUtils.addCookie(response, "wtx_cookie_id", token, "/", -1);
        }

        return token;
    }

    /**
      *@Desc 用户权限信息在redis中的key
      *@Author liuc
      *@Date 2019/2/21 12:37
      */
    public static String getQxUserRedisKey(String token){
        StringBuffer sb = new StringBuffer();
        sb.append(token).append(LoginConstants.PROJECT_CODE).append(LoginConstants.QXUSER_REDIS_KEY);
        return sb.toString();
    }
    
    /**
    * @Desc: 获取用户登录信息
    * @Author: sl 
    * @Date: 2019-02-18 11:37
    */
    public static SysUser getSysUser(){
        return getQxUser().getSysUser();
    }
    
    /**
    * @Description: 获取非超级管理员所拥有权限的项目编码，以逗号',' 隔开
    * @Author: sl 
    * @Date: 2019-02-18 14:18
    */
    public static String getUserProjectCodes(){
        QxUser user = getQxUser();
        String projectCodes = "";
        if (!"1".equals(user.getSysUser().getIsSuper())){
            for (SysProject pro : user.getProjects()){
                projectCodes += pro.getProjectCode() + ",";
            }
            projectCodes = projectCodes.substring(0, projectCodes.length() - 1);
        }
        return projectCodes;
    }
}
