package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kinghis.yyoauth.dao.SysProjectMapper;
import com.kinghis.yyoauth.pojo.SysProject;
import com.kinghis.yyoauth.service.SysProjectService;
import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Desc 项目信息
 * @Date 2019-01-21
 * @Author liuc
 */
@Service("sysProjectService")
public class SysProjectServiceImpl  implements SysProjectService {

    @Resource
    private SysProjectMapper mapper;

    @Override
    public SysProject getSysProject(String id) {
        return this.mapper.selectByPrimaryKey(id);
    }


    @Override
    public List<SysProject> queryList(SysProject sysProject, String projectCodes) {
        Example example = new Example(SysProject.class);
        Example.Criteria criteria = example.createCriteria();
        if (CommonUtil.isNotEmpty(sysProject.getStatus())){
            criteria.andEqualTo("status", sysProject.getStatus());
        }
        if(CommonUtil.isNotEmpty(sysProject.getProjectName())){
            criteria.andLike("projectName","%"+sysProject.getProjectName()+"%");
        }
        if (CommonUtil.isNotEmpty(projectCodes)){
            String [] arr = projectCodes.split(",");
            criteria.andIn("projectCode", Arrays.asList(arr));
        }
        return this.mapper.selectByExample(example);
    }

    @Override
    public DataSet<SysProject> queryPage(SysProject sysProject, Page page, String projectCodes) {
        Example example = new Example(SysProject.class);
        example.setOrderByClause("CREATE_DATE DESC");
        Example.Criteria criteria = example.createCriteria();
        if(CommonUtil.isNotEmpty(sysProject.getProjectName())){
            criteria.andLike("projectName","%"+sysProject.getProjectName()+"%");
        }
        if (CommonUtil.isNotEmpty(sysProject.getProjectCode())){
            criteria.andEqualTo("projectCode", sysProject.getProjectCode());
        }
        if (CommonUtil.isNotEmpty(sysProject.getStatus())){
            criteria.andEqualTo("status", sysProject.getStatus());
        }
        if (CommonUtil.isNotEmpty(projectCodes)){
            String [] arr = projectCodes.split(",");
            criteria.andIn("projectCode", Arrays.asList(arr));
        }
        PageHelper.startPage(page.getPageNo(), page.getPageSize());
        List<SysProject> list = this.mapper.selectByExampleAndRowBounds(example,null);
        PageInfo pageInfo = new PageInfo(list);
        String total = String.valueOf(pageInfo.getTotal());
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), Integer.parseInt(total), list);
    }

    @Override
    public int save(SysProject sysProject) {
        SysProject old = mapper.selectByPrimaryKey(sysProject.getProjectCode());
        if (null != old){
            throw new CommonException("项目编码已经存在！");
        }
        return this.mapper.insertSelective(sysProject);
    }

    @Override
    public int delete(String id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(SysProject sysProject) {
        return this.mapper.updateByPrimaryKeySelective(sysProject);
    }

    @Override
    public int updateAll(SysProject sysProject) {
        return this.mapper.updateByPrimaryKey(sysProject);
    }

    @Override
    public int deleteByIds(String ids) {
        return mapper.deleteByIds(ids);
    }

    @Override
    public List<SysProject> listProjectSelect(String projectCode, String projectCodes) {
        List<String > projectCodeList = new ArrayList();
        if (CommonUtil.isNotEmpty(projectCodes)) {
            String[] arr = projectCodes.split(",");
            projectCodeList = Arrays.asList(arr);
        }
        return this.mapper.listProjectSelect(projectCode,projectCodeList);
    }

    @Override
    public SysProject getSysProjectByCode(String sysCode) {
        SysProject project = new SysProject();
        project.setProjectCode(sysCode);
        return mapper.selectOne(project);
    }
}
