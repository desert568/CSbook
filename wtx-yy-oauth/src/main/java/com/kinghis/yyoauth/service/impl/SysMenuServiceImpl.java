package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.yyoauth.dao.SysMenuMapper;
import com.kinghis.yyoauth.pojo.SysMenu;
import com.kinghis.yyoauth.pojo.SysRolePermission;
import com.kinghis.yyoauth.service.SysMenuService;
import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Desc 菜单信息
 * @Date 2019-01-21
 * @Author liuc
 */
@Service("sysMenuService")
public class SysMenuServiceImpl  implements SysMenuService {

    @Resource
    private SysMenuMapper mapper;

    @Override
    public SysMenu getSysMenu(String id) {
        return this.mapper.selectByPrimaryKey(id);
    }


    @Override
    public List<SysMenu> queryList(SysMenu sysMenu) {
        Example example = new Example(SysMenu.class);
        example.setOrderByClause("MENU_NO ASC");
        Example.Criteria criteria = example.createCriteria();
        if (CommonUtil.isNotEmpty(sysMenu.getStatus())){
            criteria.andEqualTo("status", sysMenu.getStatus());
        }
        if (CommonUtil.isNotEmpty(sysMenu.getProjectCode())){
            criteria.andEqualTo("projectCode", sysMenu.getProjectCode());
        }
        return this.mapper.selectByExample(example);
    }

    @Override
    public DataSet<SysMenu> queryPage(SysMenu sysMenu, Page page) {
        Example example = new Example(SysMenu.class);
        Example.Criteria criteria = example.createCriteria();
        //if(!CommonUtil.isEmpty(sysMenu.getName())){
        //    criteria.andLike("name","%"+sysMenu.getName()+"%");
        //}
        List<SysMenu> list = this.mapper.selectByExampleAndRowBounds(example,page);
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), page.getTotalCount(), list);
    }

    @Override
    public int save(SysMenu sysMenu) {
        SysMenu old = mapper.selectByPrimaryKey(sysMenu.getMenuCode());
        if (null != old){
            throw new CommonException("菜单编码【"+ sysMenu.getMenuCode() +"】已存在，不可重复！");
        }
        return this.mapper.insertSelective(sysMenu);
    }

    @Override
    public int delete(String id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(SysMenu sysMenu) {
        return this.mapper.updateByPrimaryKeySelective(sysMenu);
    }

    @Override
    public int updateAll(SysMenu sysMenu) {
        return this.mapper.updateByPrimaryKey(sysMenu);
    }

    @Override
    public List<SysMenu> queryListByRole(SysRolePermission permission) {
        return mapper.queryListByRole(permission);
    }
}
