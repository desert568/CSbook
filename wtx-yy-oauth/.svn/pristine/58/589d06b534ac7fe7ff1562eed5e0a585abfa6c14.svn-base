package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.dao.SysRolePermissionMapper;
import com.kinghis.yyoauth.pojo.SysRolePermission;
import com.kinghis.yyoauth.service.SysRolePermissionService;
import com.wtx.common.util.CommonUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Desc 角色权限信息
 * @Date 2019-01-28
 * @Author liuc
 */
@Service("sysRolePermService")
public class SysRolePermissionServiceImpl  implements SysRolePermissionService {

    @Resource
    private SysRolePermissionMapper mapper;

    @Override
    public SysRolePermission getSysRolePermission(String id) {
        return this.mapper.selectByPrimaryKey(id);
    }


    @Override
    public List<SysRolePermission> queryList(SysRolePermission sysRolePerm) {
        return this.mapper.select(sysRolePerm);
    }

    @Override
    public DataSet<SysRolePermission> queryPage(SysRolePermission sysRolePerm, Page page) {
        Example example = new Example(SysRolePermission.class);
        Example.Criteria criteria = example.createCriteria();
        //if(!CommonUtil.isEmpty(sysRolePerm.getName())){
        //    criteria.andLike("name","%"+sysRolePerm.getName()+"%");
        //}
        List<SysRolePermission> list = this.mapper.selectByExampleAndRowBounds(example,page);
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), page.getTotalCount(), list);
    }

    @Override
    public int save(SysRolePermission sysRolePerm) {
        return this.mapper.insertSelective(sysRolePerm);
    }

    @Override
    public int delete(String id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(SysRolePermission sysRolePerm) {
        return this.mapper.updateByPrimaryKeySelective(sysRolePerm);
    }

    @Override
    public int updateAll(SysRolePermission sysRolePerm) {
        return this.mapper.updateByPrimaryKey(sysRolePerm);
    }

    @Override
    public void batchSave(List<SysRolePermission> list) {
        if (CommonUtil.isNotEmpty(list)){
            Example example = new Example(SysRolePermission.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("roleId", list.get(0).getRoleId());
            List<SysRolePermission> old = mapper.selectByExample(example);

            for (SysRolePermission obj : old){
                mapper.deleteByPrimaryKey(obj.getUId());
            }

            for (SysRolePermission newObj : list){
                newObj.setUId(CommonUtil.generateId());
                mapper.insertSelective(newObj);
            }
        }
    }
}
