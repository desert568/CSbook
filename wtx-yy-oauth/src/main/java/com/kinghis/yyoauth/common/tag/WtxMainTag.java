package com.kinghis.yyoauth.common.tag;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * @Desc 自定义标签主类
 * @Author liuc
 * @Date 2019/1/29 9:38
 */
@Component
public class WtxMainTag extends AbstractDialect {
    private static final String PREFIX = "wtx";//自定义标签前缀
    private static final String DIALECT_NAME_BTN = "btn";//权限按钮

    @Override
    public Set<IProcessor> getProcessors() {
        final Set<IProcessor>processors = new HashSet<>();
        processors.add(new BtnTag(DIALECT_NAME_BTN));
        return processors;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
