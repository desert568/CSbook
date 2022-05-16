package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kinghis.yyoauth.dao.SysRoleMapper;
import com.kinghis.yyoauth.pojo.SysRole;
import com.kinghis.yyoauth.service.SysRoleService;
import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Desc 角色信息
 * @Date 2019-01-21
 * @Author liuc
 */
@Service("sysRoleService")
public class SysRoleServiceImpl  implements SysRoleService {

    @Resource
    private SysRoleMapper mapper;

    @Override
    public SysRole getSysRole(String id) {
        return this.mapper.selectByPrimaryKey(id);
    }


    @Override
    public List<SysRole> queryList(SysRole sysRole, String projectCodes) {
        List<String> projectCodeList = new ArrayList<>();
        if (CommonUtil.isNotEmpty(projectCodes)){
            String[] arr = projectCodes.split(",");
            projectCodeList = Arrays.asList(arr);
        }
        return this.mapper.queryList(sysRole, projectCodeList);
    }

    @Override
    public DataSet<SysRole> queryPage(SysRole sysRole, Page page, String projectCodes) {
        /*Example example = new Example(SysRole.class);
        example.setOrderByClause("UPDATE_DATE DESC");
        Example.Criteria criteria = example.createCriteria();
        if(CommonUtil.isNotEmpty(sysRole.getRoleName())){
            criteria.andLike("roleName","%"+sysRole.getRoleName()+"%");
        }
        if (CommonUtil.isNotEmpty(sysRole.getProjectCode())){
            criteria.andEqualTo("projectCode", sysRole.getProjectCode());
        }
        if (CommonUtil.isNotEmpty(sysRole.getStatus())){
            criteria.andEqualTo("status", sysRole.getStatus());
        }*/
        //List<SysRole> list = this.mapper.selectByExampleAndRowBounds(example,page);
        List<String> projectCodeList = new ArrayList<>();
        if (CommonUtil.isNotEmpty(projectCodes)){
            String[] arr = projectCodes.split(",");
            projectCodeList = Arrays.asList(arr);
        }
        PageHelper.startPage(page.getPageNo(), page.getPageSize());
        List<SysRole> list = mapper.queryPage(null, sysRole, projectCodeList);
        PageInfo pageInfo = new PageInfo(list);
        String total = String.valueOf(pageInfo.getTotal());
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), Integer.parseInt(total), list);
    }

    @Override
    public int save(SysRole sysRole) {
        SysRole old = mapper.selectByPrimaryKey(sysRole.getRoleId());
        if (old != null){
            throw new CommonException("角色编码【"+ sysRole.getRoleId() + "】已经存在");
        }
        return this.mapper.insertSelective(sysRole);
    }

    @Override
    public int delete(String id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(SysRole sysRole) {
        return this.mapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public int updateAll(SysRole sysRole) {
        return this.mapper.updateByPrimaryKey(sysRole);
    }

    @Override
    public void deleteByIds(String ids) {
        mapper.deleteByIds(ids);
    }
}
