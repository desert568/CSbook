package com.kinghis.yyoauth.filter;


import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 去除request参数的前后空格以及防止XSS攻击
 */
@WebFilter(urlPatterns = "/*", filterName = "TrimAndXSSFilter")
@Order(value = 1)   //执行顺序，值越小越优先执行 value默认值为Integer.MAX_VALUE
public class TrimAndXSSFilter implements Filter {


    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = ((HttpServletResponse)response);
        //加入ajax可跨域
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        httpResponse.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        HttpServletRequest httpReq = (HttpServletRequest)request;
        HttpRequestWrapper wrapper = new HttpRequestWrapper(httpReq);
        chain.doFilter(wrapper, response);
    }

    public void destroy() {

    }
}
