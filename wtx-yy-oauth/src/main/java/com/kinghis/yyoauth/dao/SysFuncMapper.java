package com.kinghis.yyoauth.dao;

import com.kinghis.yyoauth.common.WtxBaseMapper;
import com.kinghis.yyoauth.pojo.SysFunc;

import java.util.List;
import java.util.Map;

public interface SysFuncMapper extends WtxBaseMapper<SysFunc> {

    /**
     * 获取用户按钮权限
     * @param params
     * @return
     */
    List<SysFunc> getFunsByUser(Map<String, Object> params);
}