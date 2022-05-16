package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.dao.SysUserProjectMapper;
import com.kinghis.yyoauth.pojo.SysUserProject;
import com.kinghis.yyoauth.service.SysUserProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Desc 用户项目权限信息
 * @Date 2019-01-21
 * @Author liuc
 */
@Service("sysUserProjectService")
public class SysUserProjectServiceImpl  implements SysUserProjectService {

    @Autowired
    private SysUserProjectMapper mapper;

    @Override
    public SysUserProject getSysUserProject(String id) {
        return this.mapper.selectByPrimaryKey(id);
    }


    @Override
    public List<SysUserProject> queryList(SysUserProject sysUserProject) {
        return this.mapper.select(sysUserProject);
    }

    @Override
    public DataSet<SysUserProject> queryPage(SysUserProject sysUserProject, Page page) {
        Example example = new Example(SysUserProject.class);
        Example.Criteria criteria = example.createCriteria();
        //if(!CommonUtil.isEmpty(sysUserProject.getName())){
        //    criteria.andLike("name","%"+sysUserProject.getName()+"%");
        //}
        List<SysUserProject> list = this.mapper.selectByExampleAndRowBounds(example,page);
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), page.getTotalCount(), list);
    }

    @Override
    public int save(SysUserProject sysUserProject) {
        return this.mapper.insertSelective(sysUserProject);
    }

    @Override
    public int delete(String id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(SysUserProject sysUserProject) {
        return this.mapper.updateByPrimaryKeySelective(sysUserProject);
    }

    @Override
    public int updateAll(SysUserProject sysUserProject) {
        return this.mapper.updateByPrimaryKey(sysUserProject);
    }
}
