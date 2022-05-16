package com.kinghis.yyoauth.dao;

import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.common.WtxBaseMapper;
import com.kinghis.yyoauth.pojo.sys_hosp;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

public interface SysHospMapper extends WtxBaseMapper<sys_hosp> {

    List<sys_hosp> selectList();


    /**
     * @Description: 管理查询列表
     * @Author: yzm
     * @Date: 2020/3/24 14:21
     */
    List<Map<String,Object>> queryPage(Page page, @Param("request") sys_hosp sys_hospys_hosp);



}