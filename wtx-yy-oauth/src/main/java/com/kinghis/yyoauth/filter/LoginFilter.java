package com.kinghis.yyoauth.filter;

import cn.trasen.BootComm.Contants;
import cn.trasen.commons.util.CookieUtils;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kinghis.common.bean.BaseContext;
import com.kinghis.common.bean.GuestSession;
import com.kinghis.common.cache.RedisService;
import com.kinghis.common.constants.CookieConstant;
import com.kinghis.common.constants.LoginConstants;
import com.kinghis.common.util.PropertiesHelper;
import com.kinghis.common.util.SSOHttpUtil;
import com.kinghis.common.util.SpringUtil;
import com.kinghis.yyoauth.feign.LoginFeignService;
import com.kinghis.yyoauth.model.QxUser;
import com.kinghis.yyoauth.model.WtxUserInfo;
import com.kinghis.yyoauth.pojo.SysUser;
import com.kinghis.yyoauth.service.LoginService;
import com.wtx.common.util.CommonUtil;
import com.wtx.common.util.HttpUtil;
import com.wtx.common.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Desc 登录过滤器
 * @Date 2019/1/23 14:38
 * @Author liuc
 */
//@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    /**
     * 上下文地址，如/wtx-yyoauth
     */
    private static String contextPath;
    /**
     * 存放过滤器地址
     */
    private static Map<String, String> filterUrlMap = new HashMap();

    /**
     * 对已经访问并认证过的url做缓存，下次再访问时直接从缓存中获取
     */
    private static Map<String, String> filterUrlCache = new HashMap();

    private static String jcpt_login_url;

    private static String jcpt_verify_url;

    /**
    * @Description: filter中加载配置文件值，只能用上下文
    * @Author: sl
    * @Date: 2021-11-05 10:14
    */
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        ServletContext servletContext = arg0.getServletContext();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        jcpt_login_url = ctx.getEnvironment().getProperty("wtxprops.jcpt_login_url");

        jcpt_verify_url = ctx.getEnvironment().getProperty("wtxprops.jcpt_verify_url");

    }

    public static String getContextPath() {
        return contextPath;
    }





    /**
     * @Desc 获取上下文
     * @Author liuc
     * @Date 2019/1/31 8:32
     */
    public static String getContextPath(HttpServletRequest httpRequest) {
        String str = httpRequest.getContextPath();
        if(CommonUtil.isEmpty(str)) {
            str = "";
        }
        if(str.endsWith("/")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * @Desc 获取请求路径，不包含上下文，如：/sysuser/user-list
     * @Author liuc
     * @Date 2019/1/31 8:32
     */
    public static String getRequestPath(HttpServletRequest request) {
        String jsp = request.getRequestURI();
        if(contextPath == null) {
            contextPath = getContextPath(request);
        }
        if(!"".equals(contextPath)) {
            int seek = jsp.indexOf(contextPath);
            if(seek == 0) {
                jsp = jsp.substring(contextPath.length());
            }
        }
        if(!jsp.startsWith("/")) {
            jsp = "/" + jsp;
        }
        return jsp;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = ((HttpServletRequest)servletRequest);
        HttpServletResponse response = ((HttpServletResponse)servletResponse);
        BaseContext.setCurrentContext(request, response);
        RedisService redisService = SpringUtil.getBean(RedisService.class);
        String token = GuestSession.getCookie();
        String reqUrl = getRequestPath(request);
        //此处不适合存内存中，当session超时时 此处还会存在
        //String filterType = filterUrlCache.get(reqUrl);
        String filterType = null;
        //if(null == filterType) {
        Iterator seek;
        String baseurl;
        boolean u = false;
        seek = filterUrlMap.keySet().iterator();
        while(seek.hasNext()) {
            baseurl = (String)seek.next();
            if(Pattern.matches(baseurl, reqUrl)) {
                filterType = filterUrlMap.get(baseurl);
                //filterUrlCache.put(reqUrl, filterType);
                u = true;
                break;
            }
        }

        //此url不需要登录认证
        if(!u) {
            filterType = "anon";
            //filterUrlCache.put(reqUrl, filterType);
        }
        //}

        //不需要鉴权的请求地址
        if(filterType != null && filterType.equals("anon")) {
            filterChain.doFilter(request,response);
            return;
        }

        String sysCode = LoginConstants.PROJECT_CODE;

        //判断是否有局部会话，如果存在局部会话直接放行
        String userId = (String)redisService.get(token + sysCode);
        if (CommonUtil.isNotEmpty(userId)){
            filterChain.doFilter(request, response);
            return;
        }

        String callback = URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");
        //登录、注销成功统一回调地址
        String loginOut = LoginConstants.loginOut_callback;


        //判断地址栏中是否有携带ticket令牌
        String ticket = request.getParameter("ticket");
        if (CommonUtil.isNotEmpty(ticket)){
            //将全局会话ticket令牌写入cookie，注销时使用
            CookieUtils.addCookie(response,CookieConstant.WTX_TICKET_ID,ticket,"/",-1);
            Map<String, String> map = new HashMap<>();
            //全局会话令牌
            map.put("ticket", ticket);
            //本地会话注销地址
            map.put("loginOutUrl", LoginConstants.localLoginOut_url);
            //SSO客户端会话的ID
            map.put("jsessionid", token + sysCode);
            String str = null;
            try {
                //请求SSO认证中心校验令牌合法性，返回用户信息
                str = SSOHttpUtil.sendHttpRequest(LoginConstants.ticket_url, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (CommonUtil.isNotEmpty(str) && !"null".equals(str)) {
                SysUser user = JSON.parseObject(str, SysUser.class);
                //保存本地会话
                redisService.set(token + sysCode, user.getUserId(),LoginConstants.loginuserTimeout);
                filterChain.doFilter(request, response);
                return;
            }
            //改变response.sendRedirect在iframe内部的问题
            PrintWriter out = response.getWriter();
            out.println("<html><script>window.open('" + LoginConstants.login_url + "?callback=" + loginOut + "','_top')</script></html>");
            return;
        }

        //判断地址中是否有集成平台参数 token, 如果有 则走集成平台登录
        String jcpt_token = request.getParameter("token");
        if (CommonUtil.isNotEmpty(jcpt_token)){
            System.out.println("token === " + jcpt_token);
            Map<String, String> params = new HashMap<>();
            params.put("token", jcpt_token);
            String str = HttpUtil.get(jcpt_verify_url, params);
            Map result = JSON.parseObject(str);
            System.out.println("token验证结果：" + JSON.toJSON(result));
            try {
                QxUser qxUser = null;
                if (result.get("code").equals("0")) {
                    Map uid = JSON.parseObject(JSONObject.toJSONString(result.get("uid")));
                    LoginFeignService loginFeignService = SpringUtil.getBean(LoginFeignService.class);
                    qxUser = loginFeignService.getQxUser(String.valueOf(uid.get("usercode")), LoginConstants.PROJECT_CODE);
                    //qxUser = loginFeignService.getQxUser("1", LoginConstants.PROJECT_CODE);
                }
                if (qxUser != null) {
                    //模拟本地登录流程
                    SysUser user = qxUser.getSysUser();
                    //生成登录凭据，以登录凭据为key，用户登录信息为value 存入redis
                    String sessionID = request.getSession().getId();
                    ticket = MD5Util.MD5Encode(sessionID + "_" + user.getLoginName(), null);

                    redisService.set(ticket, user, LoginConstants.loginuserTimeout);
                    CookieUtils.addCookie(response,CookieConstant.WTX_TICKET_ID, ticket,"/",-1);

                    //保存本地会话
                    redisService.set(WtxUserInfo.getCookie(), qxUser.getSysUser().getUserId(),LoginConstants.loginuserTimeout);
                    //filterChain.doFilter(request, response);
                    //此处使用sendRedirect,保证cookie可以
                    response.sendRedirect(loginOut);
                } else {
                    response.sendRedirect(jcpt_login_url);

                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //没有局部会话,也没有ticket令牌，则重定向到统一认证中心,检查是否有其他的系统已经登录过.
        //同时传入当前请求与登出后跳转的请求地址，如果其他系统已经登录过则继续当前请求，否则执行退出后的统一回调地址
        /*response.sendRedirect(LoginConstants.checkLogin_url + "?callback=" + callback + "&loginOutCallBack=" + loginOut);
        return;*/
        boolean isAjaxReq = cn.trasen.BootComm.utils.WebUtils.isAajxRequest(request);
        // 非AJAX请求重定向认证中心登陆URL,AJAX请求返回认证中心登陆URL
        if (!isAjaxReq) {
            response.sendRedirect(LoginConstants.checkLogin_url + "?sysCode=" + sysCode + "&callback=" + callback + "&loginOutCallBack=" + loginOut);
            return;
        } else {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("statusCode", Contants.NO_AUTHENTICATION);
            result.put("message", "登录超时，请刷新页面或重新登录！");
            result.put("object", LoginConstants.checkLogin_url);
            result.put("callback", callback);
            result.put("loginOutCallBack", loginOut);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSONUtils.toJSONString(result));
            writer.flush();
            writer.close();
            response.flushBuffer();
        }
    }

    @Override
    public void destroy() {

    }


    static {
        filterUrlMap.putAll((new PropertiesHelper("config/filter")).getAll());
    }
}
