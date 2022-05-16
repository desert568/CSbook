package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.github.pagehelper.PageInfo;
import com.kinghis.yyoauth.dao.SysHospMapper;
import com.kinghis.yyoauth.pojo.sys_hosp;
import com.kinghis.yyoauth.service.HospService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HospServiceImpl implements HospService {

    @Autowired
    private SysHospMapper sysHospMapper;

    @Override
    public DataSet<Map<String,Object>> queryPage(sys_hosp sys_hosp, Page page) {
        List<Map<String,Object>> list = sysHospMapper.queryPage(page, sys_hosp);
        PageInfo pageInfo = new PageInfo(list);
        String total = String.valueOf(pageInfo.getTotal());
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), Integer.parseInt(total), list);
    }

    @Override
    public sys_hosp getHospById(String id) {
        return sysHospMapper.selectByPrimaryKey(id);
    }


    @Override
    public void save(sys_hosp sys_hosp) {
        sysHospMapper.insertSelective(sys_hosp);
    }

    @Override
    public void update(sys_hosp sys_hosp) {
        sysHospMapper.updateByPrimaryKey(sys_hosp);
    }

    @Override
    public void deleteByIds(String ids) {
        sysHospMapper.deleteByIds(ids);
    }





}
