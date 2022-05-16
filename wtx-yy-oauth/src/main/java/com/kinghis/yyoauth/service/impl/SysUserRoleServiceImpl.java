package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.common.util.AesEncryptUtil;
import com.kinghis.yyoauth.common.config.OauthWebConfig;
import com.kinghis.yyoauth.dao.SysUserMapper;
import com.kinghis.yyoauth.dao.SysUserProjectMapper;
import com.kinghis.yyoauth.dao.SysUserRoleMapper;
import com.kinghis.yyoauth.model.api.UserRoleModel;
import com.kinghis.yyoauth.pojo.SysUser;
import com.kinghis.yyoauth.pojo.SysUserProject;
import com.kinghis.yyoauth.pojo.SysUserRole;
import com.kinghis.yyoauth.service.SysUserRoleService;
import com.kinghis.yyoauth.util.Base64Util;
import com.wtx.common.util.CommonUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Desc 用户角色信息
 * @Author liuc
 * @Date 2019-01-29 12:24
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl  implements SysUserRoleService {

    @Resource
    private SysUserRoleMapper mapper;

    @Resource
    private SysUserProjectMapper projectMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public SysUserRole getSysUserRole(String id) {
        return this.mapper.selectByPrimaryKey(id);
    }


    @Override
    public List<SysUserRole> queryList(SysUserRole sysUserRole) {
        return this.mapper.select(sysUserRole);
    }

    @Override
    public DataSet<SysUserRole> queryPage(SysUserRole sysUserRole, Page page) {
        Example example = new Example(SysUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        //if(!CommonUtil.isEmpty(sysUserRole.getName())){
        //    criteria.andLike("name","%"+sysUserRole.getName()+"%");
        //}
        List<SysUserRole> list = this.mapper.selectByExampleAndRowBounds(example,page);
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), page.getTotalCount(), list);
    }

    @Override
    public int save(SysUserRole sysUserRole) {
        return this.mapper.insertSelective(sysUserRole);
    }

    @Override
    public int delete(String id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(SysUserRole sysUserRole) {
        return this.mapper.updateByPrimaryKeySelective(sysUserRole);
    }

    @Override
    public int updateAll(SysUserRole sysUserRole) {
        return this.mapper.updateByPrimaryKey(sysUserRole);
    }

    @Override
    public void batchSave(List<SysUserRole> list) {
        if (CommonUtil.isNotEmpty(list)){
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(list.get(0).getUserId());
            List<SysUserRole> oldList = mapper.select(userRole);

            for (SysUserRole old : oldList){
                mapper.deleteByPrimaryKey(old.getUId());
            }

            for (SysUserRole obj : list){
                obj.setUId(CommonUtil.generateId());
                mapper.insertSelective(obj);

                //判断是否有当前角色所属项目的权限，如果没有则添加项目权限，如果有则不做操作
                SysUserProject userProject = new SysUserProject();
                userProject.setProjectCode(obj.getProjectCode());
                userProject.setUserId(obj.getUserId());
                List<SysUserProject> oldUserProject = projectMapper.select(userProject);
                if (CommonUtil.isEmpty(oldUserProject)){
                    userProject.setUId(CommonUtil.generateId());
                    projectMapper.insertSelective(userProject);
                }
            }
        }

    }

    @Override
    public void saveApiUserRole(UserRoleModel userRole) {
        //通过usercode查询 userid 防止本系统userid 与集成平台userid不一致的情况
        SysUser user = new SysUser();
        user.setMember_code(userRole.getUserCode());
        user = sysUserMapper.selectOne(user);

        //如果user 为空 则通过 参数中的 userid 去查询user
        if (user == null){
            user = sysUserMapper.selectByPrimaryKey(userRole.getUserId());
        }

        //如果 user 依然为 null 则新增 user
        user = new SysUser();
        user.setUserId(userRole.getUserId());
        user.setMember_code(userRole.getUserCode());
        user.setLoginName(userRole.getUserCode());
        user.setName(userRole.getUserName());

        String pwd = "123456";
        if (CommonUtil.isNotEmpty(userRole.getPassword())){
            //解密 base64密码
            pwd = Base64Util.decodeBase64(userRole.getPassword());
        }
        user.setPassword(AesEncryptUtil.encrypt(pwd, OauthWebConfig.aceKey, OauthWebConfig.aceVi));
        user.setStatus(userRole.getStatus());
        sysUserMapper.insertSelective(user);

        SysUserRole role = new SysUserRole();
        role.setUserId(user.getUserId());
        role.setProjectCode(userRole.getSysCode());
        role.setRoleId(userRole.getRoleCode());

        //新增用户项目权限
        SysUserProject project = new SysUserProject();
        project.setUserId(user.getUserId());
        project.setProjectCode(userRole.getSysCode());

        //为 Y 时为删除角色
        if ("Y".equalsIgnoreCase(userRole.getIsDeleted())){
            mapper.delete(role);

            projectMapper.delete(project);
        } else {
            //新增用户角色
            role.setUId(CommonUtil.generateId());
            mapper.insertSelective(role);

            //新增用户项目权限
            project.setUId(CommonUtil.generateId());
            projectMapper.insertSelective(project);
        }
    }
}
