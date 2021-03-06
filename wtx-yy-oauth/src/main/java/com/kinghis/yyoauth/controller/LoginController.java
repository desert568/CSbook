package com.kinghis.yyoauth.controller;

import cn.trasen.commons.util.CookieUtils;
import com.alibaba.fastjson.JSON;
import com.kinghis.common.cache.RedisService;
import com.kinghis.common.constants.CookieConstant;
import com.kinghis.common.constants.LoginConstants;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.common.model.LoginOutModel;
import com.kinghis.common.model.LoginToken;
import com.kinghis.common.util.AesEncryptUtil;
import com.kinghis.common.util.RandomNumUtil;
import com.kinghis.common.util.SSOHttpUtil;
import com.kinghis.yyoauth.model.WtxUserInfo;
import com.kinghis.yyoauth.pojo.SysProject;
import com.kinghis.yyoauth.pojo.SysUser;
import com.kinghis.yyoauth.common.config.OauthWebConfig;
import com.kinghis.yyoauth.filter.LoginFilter;
import com.kinghis.yyoauth.service.LoginService;
import com.kinghis.yyoauth.service.SysProjectService;
import com.wtx.common.util.CommonUtil;
import com.wtx.common.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Desc 登录
 * @Date 2019/1/23 15:25
 * @Author liuc
 */
@Controller
@RequestMapping("/login")
public class LoginController extends WtxBaseController{

    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private SysProjectService sysProjectService;

    /**
     *@Desc 跳转到登录页面
     *@Author liuc
     *@Date 2019/2/20 18:06
     */
    @RequestMapping("index")
    public ModelAndView toLogin(LoginToken token){
        return toView("login", token);
    }

    /**
     *@Desc 登录请求
     *@Author liuc
     *@Date 2019/2/20 18:06
     */
    @RequestMapping("login")
    public void login(HttpServletRequest request, HttpServletResponse response, LoginToken token, String sysCode) throws IOException {
        String contexPath = LoginFilter.getContextPath();
        StringBuffer sb = new StringBuffer();

        if (!RandomNumUtil.checkYzm(token.getYzm(), request)){
            token.setErrorMsg("验证码错误");
            loginError(token, contexPath, response, sysCode);
        }

        if(CommonUtil.isEmpty(token.getUsername())){
            token.setErrorMsg("用户名为空");
            loginError(token, contexPath, response, sysCode);
        }
        if(CommonUtil.isEmpty(token.getPassword())){
            token.setErrorMsg("密码为空");
            loginError(token, contexPath, response, sysCode);
        }
        //token.setPassword(AesEncryptUtil.encrypt(token.getPassword(), OauthWebConfig.aceKey, OauthWebConfig.aceVi));
        SysUser user = this.loginService.ssoLogin(token);
        if(null == user){
            token.setErrorMsg("用户名，密码错误或没有登录权限");
            loginError(token, contexPath, response, sysCode);
        }

        //生成登录凭据，以登录凭据为key，用户登录信息为value 存入redis
        String sessionID = request.getSession().getId();
        String ticket = MD5Util.MD5Encode(sessionID + "_" + token.getUsername(), null);

        /*//用户登录成功后将信息存入session
        request.getSession().setAttribute(LoginConstants.SSO_LOGIN_SESSION, user);*/

        redisService.set(ticket, user, LoginConstants.loginuserTimeout);
        //request.getSession().setAttribute(LoginConstants.SSO_TICKET, ticket);
        CookieUtils.addCookie(response,CookieConstant.WTX_TICKET_ID, ticket,"/",-1);

        //如果有回调地址则拼接回调地址返回，否则直接返回权限系统的index页面
        if (CommonUtil.isNotEmpty(token.getCallback())){
            String url = appendURL(token.getCallback(), ticket);
            response.sendRedirect(url);
            return;
        } else {
            StringBuilder url = new StringBuilder();
            url.append("?ticket=").append(ticket);
            response.sendRedirect(contexPath +"/admin/index" + url.toString());
            return;
        }
    }

    /**
     * @Desc: 检查ticket令牌有效性
     * @Author: sl
     * @Date: 2019-01-24 16:52
     */
    @RequestMapping("checkTicket")
    public void checkTicket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ticket = request.getParameter("ticket");
        SysUser user = (SysUser) redisService.get(ticket);
        if (null != user){
            //存入各子系统的登出地址用于单点退出
            String loginOutUrl = request.getParameter("loginOutUrl");
            String jsessionid = request.getParameter("jsessionid");
            String loginOutStr = (String) redisService.get(ticket + LoginConstants.SSO_LOGIN_OUT);
            List<LoginOutModel> list = null;
            if (CommonUtil.isNotEmpty(loginOutStr)){
                list = JSON.parseArray(loginOutStr, LoginOutModel.class);
            } else {
                list = new ArrayList<>();
            }

            LoginOutModel outModel = new LoginOutModel();
            outModel.setOut_url(loginOutUrl);
            outModel.setJsessionid(jsessionid);
            list.add(outModel);
            redisService.set(ticket + LoginConstants.SSO_LOGIN_OUT, JSON.toJSON(list).toString(), LoginConstants.loginuserTimeout);
        }
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSON.toJSONString(user));
    }

    /**
     * @Desc: 检查是否登录
     * @Author: sl
     * @Date: 2019-01-24 16:53
     */
    @RequestMapping("checkIsLogin")
    public void checkIsLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contexPath = LoginFilter.getContextPath();
        String sysCode = request.getParameter("sysCode");
        //业务系统当前请求地址
        String callback = request.getParameter("callback");
        //业务系统登录、注销统一回调地址
        String loginOutCallBack = request.getParameter("loginOutCallBack");
        //检查是否有其它系统登录过
        //String ticket = (String) request.getSession().getAttribute(LoginConstants.SSO_TICKET);
        String ticket = CookieUtils.getCookie(request, CookieConstant.WTX_TICKET_ID);
        if (CommonUtil.isNotEmpty(ticket)){
            String url = appendURL(callback, ticket);
            response.sendRedirect(url);
        } else {
            LoginToken token = new LoginToken();
            token.setCallback(loginOutCallBack);
            toLogin(token, contexPath, response, sysCode);
        }
    }

    /**
     *@Desc 全局会话注销
     *@Author liuc
     *@Date 2019/2/20 18:11
     */
    @RequestMapping("ssoLoginOut")
    public void loginOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ticket = request.getParameter("ticket");
        String sysCode = request.getParameter("sysCode");
        String contexPath = LoginFilter.getContextPath();
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
                //向业务系统发出注销请求
                Map<String, String> map = new HashMap<>();
                map.put("jesssionId", outModel.getJsessionid());//SSO客户端会话的ID
                SSOHttpUtil.sendHttpRequest(outModel.getOut_url(), map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        redisService.deleteKey(ticket + LoginConstants.SSO_LOGIN_OUT);
        //HttpSession session = request.getSession();
        //session.removeAttribute(LoginConstants.SSO_TICKET);
        //session.invalidate();
        CookieUtils.addCookie(response, CookieConstant.WTX_TICKET_ID, "", "/",0);
        String callback = request.getParameter("callback");
        LoginToken token = new LoginToken();
        token.setCallback(callback);
        toLogin(token, contexPath, response, sysCode);
    }

    /**
     *
     * @Desc: 本系统退出 将权限系统也当做单点登录的一个子系统，因区分单点注销和子系统注销
     * @Author: sl
     * @Date: 2019-01-31 11:05
     */
    @RequestMapping("loginOut")
    public ModelAndView localloginOut(HttpServletRequest request){
        String token = request.getParameter("jesssionId");
        String redisKey = WtxUserInfo.getQxUserRedisKey(token);
        redisService.deleteKeys(Arrays.asList(new String[]{token,redisKey}));
        return toJson();
    }

    /**
     * @Description: 登录错误时的操作 必须使用response.sendRedirect
     * @Author: sl
     * @Date: 2019-02-21 14:02
     */
    private void loginError(LoginToken token, String contexPath, HttpServletResponse response, String sysCode) throws IOException {
        String str = setLoginBackInfo(token, contexPath, sysCode);
        response.sendRedirect(str);
        return;
    }

    /**
     * @Description: 设置跳转到登录页面的相关信息
     * @Author: sl
     * @Date: 2019-02-21 14:03
     */
    private String setLoginBackInfo(LoginToken token, String contexPath, String sysCode) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        sb.append(LoginConstants.login_url + "?v=1");
        if (CommonUtil.isNotEmpty(sysCode)){
            sb.append("&sysCode=" + sysCode);
        }
        if (CommonUtil.isNotEmpty(token.getUsername())){
            sb.append("&username=" + token.getUsername());
        }
        if (CommonUtil.isNotEmpty(token.getErrorMsg())){
            sb.append("&errorMsg=" + URLEncoder.encode(token.getErrorMsg(), "UTF-8") );
        }
        if (CommonUtil.isNotEmpty(token.getCallback())){
            sb.append("&callback=" + token.getCallback());
        }
        return sb.toString();
    }

    /**
     * @Description: 登录超时时 跳转到登录页面
     * @Author: sl
     * @Date: 2019-02-21 14:03
     */
    private void toLogin(LoginToken token, String contexPath, HttpServletResponse response, String sysCode) throws IOException {
        String str = setLoginBackInfo(token, contexPath, sysCode);
        //解决response.sendRedirect 在iframe内部的问题
        PrintWriter out = response.getWriter();
        out.println("<html><script>window.open('" + str + "','_top')</script></html>");
        return;
    }


    private String appendURL(String callback, String ticket){
        StringBuilder url = new StringBuilder();
        url.append(callback);
        if (0 <= callback.indexOf("?")) {
            url.append("&");
        } else {
            url.append("?");
        }
        url.append("ticket=").append(ticket);
        return url.toString();
    }

    @RequestMapping("getProjectNameByCode")
    public ModelAndView getProjectNameByCode(String sysCode){
        SysProject project = sysProjectService.getSysProjectByCode(sysCode);
        return toJson(project);
    }
}
