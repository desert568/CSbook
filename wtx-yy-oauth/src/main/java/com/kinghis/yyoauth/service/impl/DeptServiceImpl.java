package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.github.pagehelper.PageInfo;
import com.kinghis.yyoauth.dao.DictDeptMapper;
import com.kinghis.yyoauth.dao.DictMapper;
import com.kinghis.yyoauth.model.api.DeptModel;
import com.kinghis.yyoauth.pojo.Dict_dept;
import com.kinghis.yyoauth.pojo.T_jxkh_dept;
import com.kinghis.yyoauth.service.DeptService;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private DictDeptMapper dictDeptMapper;

    @Override
    public DataSet<Map<String,Object>> queryPage(T_jxkh_dept t_jxkh_dept, Page page) {
        List<Map<String,Object>> list = dictMapper.queryPage(page, t_jxkh_dept);
        PageInfo pageInfo = new PageInfo(list);
        String total = String.valueOf(pageInfo.getTotal());
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), Integer.parseInt(total), list);
    }


    @Override
    public Dict_dept getDeptById(String id) {
        return dictDeptMapper.selectByPrimaryKey(id);
    }

    @Override
    public void save(Dict_dept t_jxkh_dept) {
        t_jxkh_dept.setStandard_id(CommonUtil.generateId());
        dictDeptMapper.insertSelective(t_jxkh_dept);
    }

    @Override
    public void update(Dict_dept t_jxkh_dept) {
        dictDeptMapper.updateByPrimaryKey(t_jxkh_dept);
    }

    @Override
    public void deleteByIds(String ids) {
        dictDeptMapper.deleteByIds(ids);
    }

    @Override
    public List<T_jxkh_dept> listDeptSelect(String org_code) {
        List<T_jxkh_dept> lists= new ArrayList<>();
        Dict_dept dept = new Dict_dept();
        if (CommonUtil.isNotEmpty(org_code)){
            dept.setOrg_code(org_code);
        }
       List<Map<String,Object>> list= dictDeptMapper.queryPage(dept);
       for(int i=0;i<list.size();i++){
           T_jxkh_dept t_jxkh_dept= new T_jxkh_dept();
           t_jxkh_dept.setDept_code(String.valueOf(list.get(i).get("dept_code")));
           t_jxkh_dept.setDept_name(String.valueOf(list.get(i).get("dept_name")));
           lists.add(t_jxkh_dept);
       }

       return  lists;

    }


    @Override
    public void saveApiDept(DeptModel dept) {
        T_jxkh_dept old = new T_jxkh_dept();
        old.setDept_code(dept.getDeptCode());
        dictMapper.delete(old);

        old.setDept_name(dept.getDeptName());
        old.setOrg_code(dept.getCorpCode());
        old.setOrg_name(dept.getOrgFlag());
        old.setIsflag("0");

        old.setId(CommonUtil.generateId());

        dictMapper.insertSelective(old);
    }

}
