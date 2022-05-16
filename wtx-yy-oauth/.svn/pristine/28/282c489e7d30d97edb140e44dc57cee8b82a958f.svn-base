package com.kinghis.yyoauth.dao;

import com.kinghis.yyoauth.common.WtxBaseMapper;
import com.kinghis.yyoauth.model.AreaModel;
import com.kinghis.yyoauth.pojo.SysArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface AreaMapper extends WtxBaseMapper<SysArea> {

    /**
     * @description: 地区查询
     * @author ydd
     * @date 2022-04-12 9:49
     */
    List<Map<String,Object>> queryPage( @Param("request") AreaModel sysArea);


    /**
     * @description: 地区查询
     * @author ydd
     * @date 2022-04-12 9:49
     */
    List<Map<String,Object>> query( @Param("request") SysArea sysArea);

}
