package com.kinghis.yyoauth.controller;

import com.kinghis.common.bean.GuestSession;
import com.kinghis.common.cache.RedisService;
import com.kinghis.common.constants.LoginConstants;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.yyoauth.common.config.OauthWebConfig;
import com.kinghis.yyoauth.model.QxUser;
import com.kinghis.yyoauth.model.WtxUserInfo;
import com.kinghis.yyoauth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
* @Desc: 后台管理系统首页
* @Author: sl 
* @Date: 2019-01-22 9:40
*/
@Controller
@RequestMapping("admin")
public class AdminController extends WtxBaseController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisService redisService;
    
    /** 
    * @Desc: 后台管理系统首页
    * @Author: sl 
    * @Date: 2019-01-22 9:29
    */
    @RequestMapping("/index")
    @ResponseBody
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response){
        QxUser qxUser = WtxUserInfo.getQxUser();
        if (null == qxUser){
            //此处获取菜单权限，资源权限等信息，并存入缓存
            String token = GuestSession.getCookie() + OauthWebConfig.project_code;
            String userId = (String)redisService.get(token);
            qxUser = loginService.getQxUser(userId, OauthWebConfig.project_code);
            String redisKey = WtxUserInfo.getQxUserRedisKey(token);
            redisService.set(redisKey, qxUser, LoginConstants.loginuserTimeout);
        }
        return toView("index", OauthWebConfig.loginOut_url,
                OauthWebConfig.loginOut_callback, qxUser.getSysUser());
    }
    

}
