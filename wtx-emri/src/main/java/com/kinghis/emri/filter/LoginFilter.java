package com.kinghis.emri.filter;

import cn.trasen.BootComm.Contants;
import com.alibaba.druid.support.json.JSONUtils;
import com.kinghis.common.constants.LoginConstants;
import com.kinghis.common.util.PropertiesHelper;
import com.wtx.common.util.CommonUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2021-11-26 9:13
 */
public class LoginFilter implements Filter {

    private static String contextPath;

    private static Map<String, String> filterUrlMap = new HashMap();

    static {
        filterUrlMap.putAll((new PropertiesHelper("config/filter")).getAll());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = ((HttpServletRequest)servletRequest);
        HttpServletResponse response = ((HttpServletResponse)servletResponse);
        String reqUrl = getRequestPath(request);
        if (reqUrl.equals("/admin/index")){
            filterChain.doFilter(request,response);
            return;
        }

        String filterType = null;
        Iterator seek;
        String baseurl;
        boolean u = false;
        seek = filterUrlMap.keySet().iterator();
        while(seek.hasNext()) {
            baseurl = (String)seek.next();
            if(Pattern.matches(baseurl, reqUrl)) {
                filterType = filterUrlMap.get(baseurl);
                u = true;
                break;
            }
        }

        //此url不需要登录认证
        if(!u) {
            filterType = "anon";
        }

        if(filterType != null && filterType.equals("anon")) {
            filterChain.doFilter(request,response);
            return;
        }

        String userName = (String) request.getSession().getAttribute("login_name");
        if (CommonUtil.isEmpty(userName)){
            boolean isAjaxReq = cn.trasen.BootComm.utils.WebUtils.isAajxRequest(request);
            // 非AJAX请求重定向认证中心登陆URL,AJAX请求返回认证中心登陆URL
            if (!isAjaxReq) {
                response.sendRedirect("/admin/index");
                return;
            } else {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("statusCode", Contants.NO_AUTHENTICATION);
                result.put("message", "登录超时，请刷新浏览器重新登录！");
                result.put("object", LoginConstants.checkLogin_url);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSONUtils.toJSONString(result));
                writer.flush();
                writer.close();
                response.flushBuffer();
            }
        } else {
            filterChain.doFilter(request,response);
            return;
        }
    }

    @Override
    public void destroy() {

    }

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
}
