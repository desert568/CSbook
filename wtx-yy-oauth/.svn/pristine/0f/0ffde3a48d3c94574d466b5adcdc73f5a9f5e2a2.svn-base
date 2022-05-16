package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.dao.AreaMapper;
import com.kinghis.yyoauth.dao.DictDeptMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kinghis.yyoauth.dao.DictOrganizationMapper;
import com.kinghis.yyoauth.dao.SysHospMapper;
import com.kinghis.yyoauth.pojo.*;
import com.kinghis.yyoauth.service.DictOrganizationService;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @Desc 机构信息
 * @Date 2019-01-24
 * @Author liuc
 */
@Service("dictOrganizationService")
public class DictOrganizationServiceImpl implements DictOrganizationService {

    @Autowired
    private DictOrganizationMapper mapper;

    @Autowired
    private DictDeptMapper deptMapper;

    @Autowired
    private SysHospMapper sysHospMapper;


    @Autowired
    private com.kinghis.yyoauth.dao.AreaMapper areaMapper;




    @Override
    public DictOrganization getDictOrganization(String id) {
        return this.mapper.selectByPrimaryKey(id);
    }


    @Override
    public List<DictOrganization> queryList(DictOrganization dictOrganization) {
        return this.mapper.select(dictOrganization);

    }

    @Override
    public DataSet<DictOrganization> queryPage(DictOrganization dictOrganization, Page page) {
        Example example = new Example(DictOrganization.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("ORG_CODE DESC");
        /*if (!CommonUtil.isEmpty(dictOrganization.getShortName())) {
            criteria.andLike("shortName", "%" + dictOrganization.getShortName() + "%");
        }*/
        if (CommonUtil.isNotEmpty(dictOrganization.getOrgCode())) {
            criteria.andEqualTo("orgCode", dictOrganization.getOrgCode());
        }
        /*if (CommonUtil.isNotEmpty(dictOrganization.getStatus())) {
            criteria.andEqualTo("status", dictOrganization.getStatus());
        }*/
        PageHelper.startPage(page.getPageNo(), page.getPageSize());
        List<DictOrganization> list = this.mapper.selectByExampleAndRowBounds(example, null);
        PageInfo pageInfo = new PageInfo(list);
        String total = String.valueOf(pageInfo.getTotal());
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), Integer.parseInt(total), list);
    }

    @Override
    public int save(DictOrganization dictOrganization) {
        String[] string1 = dictOrganization.getAreaCode().split("/");
        dictOrganization.setAreaCodeshort(string1[2]);
        dictOrganization.setAreaName(areaMapper.selectByPrimaryKey(string1[2]).getAreaName());
        return this.mapper.insertSelective(dictOrganization);
    }

    @Override
    public int delete(String id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(DictOrganization dictOrganization) {
        String[] string1 = dictOrganization.getAreaCode().split("/");
        dictOrganization.setAreaCodeshort(string1[2]);
        dictOrganization.setAreaName(areaMapper.selectByPrimaryKey(string1[2]).getAreaName());
        return this.mapper.updateByPrimaryKeySelective(dictOrganization);
    }

    @Override
    public int updateAll(DictOrganization dictOrganization) {
        return this.mapper.updateByPrimaryKey(dictOrganization);
    }

    @Override
    public int deleteByIds(String ids) {
        return this.mapper.deleteByIds(ids);
    }

    @Override
    public List<DictOrganization> listFormSelect(String orgCode) {
        DictOrganization dictOrganization = new DictOrganization();
        dictOrganization.setOrgCode(orgCode);
        if (orgCode == null || "".equals(orgCode)){
            return this.mapper.selectAll();
        }
        return this.mapper.select(dictOrganization);
    }

    @Override
    public List<Dict_dept> listDeptSelect() {
        return this.deptMapper.selectAll();
    }

    @Override
    public List<sys_hosp> listHospSelect(String org_code) {
        sys_hosp hosp = new sys_hosp();
        if (CommonUtil.isNotEmpty(org_code)){
            hosp.setOrg_code(org_code);
        }
        return this.sysHospMapper.select(hosp);
    }


    @Override
    public  List<Map<String,Object>> listAreaSelect(String code){

        List<Map<String,Object>> rlist = new ArrayList<>();
        SysArea sysArea = new  SysArea();
        sysArea.setLevel("1");
        sysArea.setAreaCode(code);
        List<Map<String,Object>> list = areaMapper.query(sysArea);
        if (list == null) {
            list = new ArrayList<>();
        }

        for (int i = 0; i < list.size(); i++) {
            Map<String,Object> m = list.get(i);
            SysArea sysArea1 = new  SysArea();
            if (!ObjectUtils.isEmpty(m)){
                sysArea1.setParentCode(String.valueOf(m.get("value")));
                List<Map<String,Object>> list_children = areaMapper.query(sysArea1);
                List<Map<String,Object>> list_children_new = new ArrayList<>();
                if (!ObjectUtils.isEmpty(list_children)){
                    for (int j = 0; j< list_children.size(); j++) {
                        Map<String,Object> m2 = list_children.get(j);
                        SysArea sysArea2 = new  SysArea();
                        sysArea2.setParentCode(String.valueOf(m2.get("value")));
                        List<Map<String,Object>> list_children2 = areaMapper.query(sysArea2);
                        m2.put("children",list_children2);
                        list_children_new.add(m2);
                    }
                    m.put("children",list_children_new);
                    rlist.add(m);
                }

            }
        }
        return rlist;
    }
}
