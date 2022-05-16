package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.model.AreaModel;
import com.kinghis.yyoauth.pojo.SysArea;

import java.util.Map;

/**
 * @author Administrator
 * @title: AreaService
 * @projectName ba5.0
 * @description: TODO
 * @date 2022-04-129:45
 */
public interface AreaService {

    /**
     * @description: 地区管理查询
     * @author ydd
     * @date 2022-04-12 9:46
     */
    DataSet<Map<String,Object>> queryPage(AreaModel sysArea);
}
