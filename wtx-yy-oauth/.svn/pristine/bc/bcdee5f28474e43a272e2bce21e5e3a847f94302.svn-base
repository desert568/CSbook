package com.kinghis.yyoauth.common.config;

import com.kinghis.common.constants.LoginConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Desc 自定义配置
 * @Date 2019/1/24 17:05
 * @Author liuc
 */
@Configuration
public class OauthWebConfig {

    /**
     * 项目编码
     */
    public static String project_code;


    /**
     * 本地会话缓存超时时间
     */
    public static Long  loginuserTimeout;

    /**
     * 用户密码加密密钥
     */
    public static String aceKey;
    public static String aceVi;

    /**
     * SSO认证中心登录地址
     */
    public static String login_url;

    /**
     * 登录令牌校验地址
     */
    public static String ticket_url;


    /**
     * 检查登录状态地址
     */
    public static String checkLogin_url;

    /**
     * 全局会话注销地址
     */
    public static String loginOut_url;

    /**
     * SSO认证中心登录、退出统一回调地址
     */
    public static String loginOut_callback;

    /**
     * 本地会话注销地址
     */
    public static String localLoginOut_url;


    /**
     *  pdf密钥
     */
    public static String pdfPwd;


    public static String path;


    @Autowired
    public void setLoginuserTimeout(@Value("${wtxprops.redis.loginuserTimeout}")String loginuserTimeout) {
        LoginConstants.loginuserTimeout = Long.parseLong(loginuserTimeout);
        OauthWebConfig.loginuserTimeout = Long.parseLong(loginuserTimeout);
    }

    @Autowired
    public void setAceKey(@Value("${wtxprops.ace.key}")String aceKey) {
        OauthWebConfig.aceKey = aceKey;
    }


    @Autowired
    public void setAceVi(@Value("${wtxprops.ace.vi}")String aceVi) {
        OauthWebConfig.aceVi = aceVi;
    }

    @Autowired
    public void setProject_code(@Value("${wtxprops.project_code}") String project_code) {
        LoginConstants.PROJECT_CODE = project_code;
        OauthWebConfig.project_code = project_code;
    }

    @Autowired
    public void setPdfPwd(@Value("${wtxprops.file.pdfPwd}") String pdfPwd) {
        OauthWebConfig.pdfPwd = pdfPwd;
    }

    @Autowired
    public  void setPath(@Value("${wtxprops.file.path}")String path) {
        OauthWebConfig.path = path;
    }

    @Autowired
    public void setSso_url(@Value("${wtxprops.login.sso_url}") String sso_url) {
        if(!sso_url.endsWith("/")) {
            sso_url = sso_url + "/";
        }
        //登录地址(SSO系统地址)
        OauthWebConfig.login_url = sso_url + "login/index";
        //全局会话注销地址(SSO系统地址)
        OauthWebConfig.loginOut_url = sso_url + "login/ssoLoginOut";
        //ticket校验地址(SSO系统地址)
        OauthWebConfig.ticket_url = sso_url + "login/checkTicket";
        //检查其他系统是否已登录(SSO系统地址)
        OauthWebConfig.checkLogin_url = sso_url + "login/checkIsLogin";

        LoginConstants.login_url = OauthWebConfig.login_url;
        LoginConstants.loginOut_url = OauthWebConfig.loginOut_url;
        LoginConstants.ticket_url = OauthWebConfig.ticket_url;
        LoginConstants.checkLogin_url = OauthWebConfig.checkLogin_url;
    }

    @Autowired
    public void setLocal_app_url(@Value("${wtxprops.login.local_app_url}") String local_app_url) {
        if(!local_app_url.endsWith("/")) {
            local_app_url = local_app_url + "/";
        }
        //全局会话注销回调地址(业务系统地址)
        OauthWebConfig.loginOut_callback = local_app_url + "admin/index";
        //本地会话注销地址(业务系统地址)
        OauthWebConfig.localLoginOut_url = local_app_url + "login/loginOut";

        LoginConstants.loginOut_callback = OauthWebConfig.loginOut_callback;
        LoginConstants.localLoginOut_url = OauthWebConfig.localLoginOut_url;
    }
}
