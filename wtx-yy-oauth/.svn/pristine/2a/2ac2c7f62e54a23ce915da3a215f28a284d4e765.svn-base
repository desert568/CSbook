package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kinghis.yyoauth.dao.FtpMapper;
import com.kinghis.yyoauth.pojo.T_ftp_info;
import com.kinghis.yyoauth.service.FtpService;
import com.wtx.common.util.CommonUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2021-01-21 9:58
 */
@Service
public class FtpServiceImpl implements FtpService {

    @Resource
    private FtpMapper ftpMapper;

    @Override
    public DataSet<T_ftp_info> queryPage(T_ftp_info ftp, Page page) {
        List<T_ftp_info> list = ftpMapper.queryPage(page, ftp);
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), page.getTotalCount(), list);
    }

    @Override
    public T_ftp_info getFtpInfo(String id) {
        return ftpMapper.selectByPrimaryKey(id);
    }

    @Override
    public void save(T_ftp_info ftp) {
        ftp.setId(CommonUtil.generateId());
        ftpMapper.insertSelective(ftp);
    }

    @Override
    public void update(T_ftp_info ftp) {
        ftpMapper.updateByPrimaryKeySelective(ftp);
    }

    @Override
    public void deleteByIds(String ids) {
        ftpMapper.deleteByIds(ids);
    }
}
