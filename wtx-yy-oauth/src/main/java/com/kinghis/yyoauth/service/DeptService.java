package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.model.api.DeptModel;
import com.kinghis.yyoauth.pojo.Dict_dept;
import com.kinghis.yyoauth.pojo.T_jxkh_dept;

import java.util.List;
import java.util.Map;

public interface DeptService {

    /**
     * @Description: 科室管理查询列表
     * @Author: yzm
     * @Date: 2020/3/24 14:18
     */
    DataSet<Map<String,Object>> queryPage(T_jxkh_dept T_jxkh_dept, Page page);

    /**
     * @Description: 通过科室id查询
     * @Author: yzm
     * @Date: 2020/3/24 15:24 
     */
    Dict_dept getDeptById(String id);

    void save(Dict_dept t_jxkh_dept);

    void update(Dict_dept t_jxkh_dept);

    void deleteByIds(String ids);

    List<T_jxkh_dept> listDeptSelect(String org_code);

    void saveApiDept(DeptModel dept);
}
