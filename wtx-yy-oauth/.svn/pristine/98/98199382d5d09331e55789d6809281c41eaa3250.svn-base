package com.kinghis.yyoauth.common.config;

import com.kinghis.common.cache.RedisService;
import com.kinghis.common.convert.DateConvert;
import com.kinghis.common.util.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Desc 第三方bean的初始化
 * @Date 2019/1/25 19:58
 * @Author liuc
 */
@Configuration
public class InitBeanConfig {

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

    @Bean
    public RedisService redisService() {
        return new RedisService();
    }

    @Bean
    public DateConvert dateConvert(){
        return new DateConvert();
    }
}
