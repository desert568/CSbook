package com.kinghis.yyoauth.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.common.WtxBaseMapper;
import com.kinghis.yyoauth.pojo.Dict_dept;
import com.kinghis.yyoauth.pojo.T_jxkh_dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface DictDeptMapper extends WtxBaseMapper<Dict_dept> {

    /**
     * @Description: 科室管理查询列表
     * @Author: yzm
     * @Date: 2020/3/24 14:21
     */
    List<Map<String,Object>> queryPage( @Param("request") Dict_dept Dict_dept);

}
