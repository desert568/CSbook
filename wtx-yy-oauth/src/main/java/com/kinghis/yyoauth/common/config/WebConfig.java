package com.kinghis.yyoauth.common.config;

import com.kinghis.yyoauth.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc 过滤器配置
 * @Date 2019/2/21 14:07
 * @Author liuc
 */
@Configuration
public class WebConfig {

    /**
      *@Desc 登录过滤器配置
      *@Author liuc
      *@Date 2019/2/21 14:16
      */
    @Bean
    public FilterRegistrationBean loginFilter(){
        //创建 过滤器注册bean
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        LoginFilter loginFilter = new LoginFilter();
        registrationBean.setFilter(loginFilter);
        List urls = new ArrayList();
        urls.add("/*");
        //设置 有效url
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }
}
