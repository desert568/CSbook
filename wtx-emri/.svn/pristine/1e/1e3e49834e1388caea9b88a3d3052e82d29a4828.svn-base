package com.kinghis.emri.config;

import com.kinghis.common.cache.RedisService;
import com.kinghis.common.convert.DateConvert;
import com.kinghis.common.util.SpringUtil;
import com.kinghis.emri.filter.LoginFilter;
import com.kinghis.emri.filter.TrimAndXSSFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class BeanConfig {

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    public RedisService redisService() {
        return new RedisService();
    }

    @Bean
    public DateConvert dateConvert() {
        return new DateConvert();
    }

    @Bean
    public FilterRegistrationBean xssFilter(){
        //创建 过滤器注册bean
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TrimAndXSSFilter xssFilter = new TrimAndXSSFilter();
        registrationBean.setFilter(xssFilter);
        //需放置在最前面执行
        registrationBean.setOrder(1);
        List urls = new ArrayList();
        urls.add("/*");
        //设置 有效url
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }

    @Bean
    @Conditional(LoginCondition.class)
    public FilterRegistrationBean loginFilter(){
        //创建 过滤器注册bean
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        LoginFilter loginFilter = new LoginFilter();
        registrationBean.setFilter(loginFilter);
        registrationBean.setOrder(Integer.MAX_VALUE);
        List urls = new ArrayList();
        urls.add("/*");
        //设置 有效url
        registrationBean.setUrlPatterns(urls);
        System.out.println("启用登录拦截器。。。。");
        return registrationBean;
    }
}
