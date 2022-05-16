package com.kinghis.yyoauth.common;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

public interface WtxBaseMapper<T> extends Mapper<T>, IdsMapper<T> {
}