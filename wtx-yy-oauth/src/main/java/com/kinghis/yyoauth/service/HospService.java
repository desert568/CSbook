package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.pojo.sys_hosp;

import java.util.Map;

public interface HospService {

    /**
     * @Description: 管理查询列表
     * @Author: yzm
     * @Date: 2020/3/24 14:18
     */
    DataSet<Map<String,Object>> queryPage(sys_hosp sys_hosp, Page page);


    sys_hosp getHospById(String id);
    void save(sys_hosp sys_hosp);

    void update(sys_hosp sys_hosp);

    void deleteByIds(String ids);


}
