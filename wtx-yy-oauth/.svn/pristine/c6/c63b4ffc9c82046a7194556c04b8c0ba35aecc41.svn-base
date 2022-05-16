package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.dao.SysLoginLogMapper;
import com.kinghis.yyoauth.pojo.SysLoginLog;
import com.kinghis.yyoauth.service.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Desc 登录日志
 * @Date 2019-01-21
 * @Author liuc
 */
@Service("sysLoginLogService")
public class SysLoginLogServiceImpl  implements SysLoginLogService {

    @Autowired
    private SysLoginLogMapper mapper;

    @Override
    public SysLoginLog getSysLoginLog(String id) {
        return this.mapper.selectByPrimaryKey(id);
    }


    @Override
    public List<SysLoginLog> queryList(SysLoginLog sysLoginLog) {
        return this.mapper.select(sysLoginLog);
    }

    @Override
    public DataSet<SysLoginLog> queryPage(SysLoginLog sysLoginLog, Page page) {
        Example example = new Example(SysLoginLog.class);
        Example.Criteria criteria = example.createCriteria();
        //if(!CommonUtil.isEmpty(sysLoginLog.getName())){
        //    criteria.andLike("name","%"+sysLoginLog.getName()+"%");
        //}
        List<SysLoginLog> list = this.mapper.selectByExampleAndRowBounds(example,page);
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), page.getTotalCount(), list);
    }

    @Override
    public int save(SysLoginLog sysLoginLog) {
        return this.mapper.insertSelective(sysLoginLog);
    }

    @Override
    public int delete(String id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(SysLoginLog sysLoginLog) {
        return this.mapper.updateByPrimaryKeySelective(sysLoginLog);
    }

    @Override
    public int updateAll(SysLoginLog sysLoginLog) {
        return this.mapper.updateByPrimaryKey(sysLoginLog);
    }
}
