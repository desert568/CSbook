package com.kinghis.emri.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kinghis.emri.dao.SourceMapper;
import com.kinghis.emri.pojo.T_emri_source;
import com.kinghis.emri.service.SourceService;
import com.kinghis.emri.util.ConnUtil;
import com.wtx.common.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-10 9:38
 */
@Service
public class SourceServiceImpl implements SourceService {

    @Resource
    private SourceMapper sourceMapper;

    @Override
    public DataSet<T_emri_source> queryPage(T_emri_source request, Page page) {
        PageHelper.startPage(page.getPageNo(), page.getPageSize());
        List<T_emri_source> list = sourceMapper.queryList(request);
        PageInfo pageInfo = new PageInfo(list);
        String total = String.valueOf(pageInfo.getTotal());
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), Integer.parseInt(total), pageInfo.getList());
    }

    @Override
    public T_emri_source getById(String source_code) {
        return sourceMapper.selectByPrimaryKey(source_code);
    }

    @Override
    public void add(T_emri_source model) {
        model.setUpdate_time(new Date());
        if (CommonUtil.isEmpty(model.getSource_code())) {
            model.setSource_code(CommonUtil.generateId());
            sourceMapper.insertSelective(model);
        } else {
            sourceMapper.updateByPrimaryKey(model);
        }
    }

    @Override
    public void delete(String ids) {
        sourceMapper.deleteByIds(ids);
    }

    @Override
    public int testConnect(T_emri_source source) throws SQLException {
        Connection conn = ConnUtil.getConnection(source);
        if (conn != null) {
            conn.close();
            return 1;
        }
        return 0;
    }

    @Override
    public List<T_emri_source> listSource() {
        return sourceMapper.selectAll();
    }
}
