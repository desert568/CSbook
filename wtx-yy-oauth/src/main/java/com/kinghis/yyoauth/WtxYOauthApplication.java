package com.kinghis.yyoauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@EnableEurekaClient
@MapperScan(basePackages = "com.kinghis.yyoauth.dao")
@ComponentScan(basePackages = {"com.kinghis.yyoauth","com.kinghis.common.cache"})
@EnableFeignClients(basePackages = {"com.kinghis.yyoauth.feign"} )
@ServletComponentScan
@SpringBootApplication
@EnableAutoConfiguration
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})

public class WtxYOauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(WtxYOauthApplication.class, args);
	}

}

