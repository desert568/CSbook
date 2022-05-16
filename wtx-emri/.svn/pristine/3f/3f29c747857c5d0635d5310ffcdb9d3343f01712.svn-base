package com.kinghis.emri.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kinghis.emri.dao.ErrorMapper;
import com.kinghis.emri.pojo.T_emri_error;
import com.kinghis.emri.service.ErrorService;
import com.wtx.common.util.CommonUtil;
import com.wtx.common.util.DateHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-10 17:29
 */
@Service
public class ErrorServiceImpl implements ErrorService {

    @Resource
    private ErrorMapper errorMapper;

    @Override
    public void add(T_emri_error error) {
        if (CommonUtil.isEmpty(error.getId())) {
            error.setId(CommonUtil.generateId());
        }
        if (CommonUtil.isEmpty(error.getOper_date())) {
            error.setOper_date(DateHelper.formateDate(new Date(), "yyyy-MM-dd hh:mm"));
        }
        errorMapper.insertSelective(error);
    }


    @Override
    public DataSet<T_emri_error> queryError(T_emri_error request, Page page) {
        PageHelper.startPage(page.getPageNo(), page.getPageSize());
        List<T_emri_error> list = errorMapper.queryList(request);
        PageInfo pageInfo = new PageInfo(list);
        String total = String.valueOf(pageInfo.getTotal());
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), Integer.parseInt(total), pageInfo.getList());
    }

    @Override
    public T_emri_error getById(String id) {
        return errorMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteAll(T_emri_error request) {
        errorMapper.deleteAll(request);
    }
}
