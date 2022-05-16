package com.kinghis.emri.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @DESC: 用于判断是否启用登录拦截器，在拦截器注入bean上 加入@Conditional(LoginCondition.class)
 *          LoginCondition.class为条件判断的class
 * @Author: sl
 * @Date: 2021-12-16 10:07
 */
public class LoginCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String use_login = context.getEnvironment().getProperty("use_login");
        if ("1".equals(use_login)){
            return true;
        }
        return false;
    }
}
