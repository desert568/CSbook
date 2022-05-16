package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.kinghis.yyoauth.dao.AreaMapper;
import com.kinghis.yyoauth.model.AreaModel;
import com.kinghis.yyoauth.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @title: AreaServiceImpl
 * @projectName ba5.0
 * @description: TODO
 * @date 2022-04-129:47
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public DataSet<Map<String, Object>> queryPage(AreaModel sysArea) {
        PageHelper.startPage(sysArea.getPageIndex(), sysArea.getPageSize());
        List<Map<String,Object>> list = areaMapper.queryPage(sysArea);
        PageInfo pageInfo = new PageInfo(list);
        String total = String.valueOf(pageInfo.getTotal());
        return new DataSet<>(sysArea.getPageIndex(), sysArea.getPageSize(), pageInfo.getPages(), Integer.parseInt(total), list);
    }
}