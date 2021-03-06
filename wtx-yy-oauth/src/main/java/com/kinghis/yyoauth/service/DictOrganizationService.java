package com.kinghis.yyoauth.service;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.pojo.*;

import java.util.List;
import java.util.Map;

/**
 * @Desc 机构信息
 * @Date 2019-01-24
 * @Author liuc
 */
public interface DictOrganizationService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    DictOrganization getDictOrganization(String id);

    /**
     * 根据条件返回集合
     * @param dictOrganization
     * @return
     */
    List<DictOrganization> queryList(DictOrganization dictOrganization);

    /**
     * 根据条件分页查询
     * @param page
     * @param dictOrganization
     * @return
     */
    DataSet<DictOrganization> queryPage(DictOrganization dictOrganization, Page page);


    /**
     * 保存
     * @param dictOrganization
     * @return
     */
    int save(DictOrganization dictOrganization);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 更新非空字段
     * @param dictOrganization
     * @return
     */
    int update(DictOrganization dictOrganization);

    /**
     * 更新所有字段，包括空和非空
     * @param dictOrganization
     * @return
     */
    int updateAll(DictOrganization dictOrganization);


    /**
     *@Desc 根据主键@Id进行删除，多个Id以逗号,分割
     *@Author yzm
     *@Date 2019/1/29 
     */
    int deleteByIds(String ids);

    /** 
    * @Description: 机构列表查询框
    * @Author: cp 
    * @Date: 2019/9/12 9:54
    */
    List<DictOrganization> listFormSelect(String orgCode);

    /**
     * @DESC: 科室下拉框查询
     * @Author: cp
     * @Date: 2019/11/4 15:55
     */
    List<Dict_dept> listDeptSelect();


    /**
     * @DESC: 所属院区下拉框查询
     * @Author: gq
     * @Date: 2020/03/19 15:55
     */
    List<sys_hosp> listHospSelect(String org_code);

    List<Map<String,Object>>  listAreaSelect(String code);

}
