package com.kinghis.yyoauth.service.impl;

import tk.mybatis.mapper.common.Mapper;

/**
 * @Desc
 * @Date 2019/1/18 12:31
 * @Author liuc
 */
public abstract class BaseServiceImpl<T extends Mapper<E>,E> {


    public abstract T getMapper();

    E getObject(String id) {
        return getMapper().selectByPrimaryKey(id);
    }

}
