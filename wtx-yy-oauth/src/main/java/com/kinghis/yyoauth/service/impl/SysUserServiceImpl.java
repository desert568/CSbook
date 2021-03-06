package com.kinghis.yyoauth.service.impl;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kinghis.common.util.AesEncryptUtil;
import com.kinghis.yyoauth.common.config.OauthWebConfig;
import com.kinghis.yyoauth.dao.SysUserMapper;
import com.kinghis.yyoauth.model.api.DeptModel;
import com.kinghis.yyoauth.model.api.MemberModel;
import com.kinghis.yyoauth.model.api.PasswordModel;
import com.kinghis.yyoauth.pojo.SysUser;
import com.kinghis.yyoauth.service.SysUserService;
import com.kinghis.yyoauth.util.Base64Util;
import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Desc 用户信息
 * @Author liuc
 * @Date 2019/1/17 17:54
 */
@Service("sysUserService")
public class SysUserServiceImpl  implements SysUserService {

    @Resource
    private SysUserMapper mapper;

    @Override
    public SysUser getSysUser(String id) {
        return this.mapper.selectByPrimaryKey(id);
    }


    @Override
    public List<SysUser> queryList(SysUser sysUser) {
        return this.mapper.select(sysUser);
    }

    @Override
    public DataSet<SysUser> queryPage(SysUser sysUser, Page page) {
        Example example = new Example(SysUser.class);
        //排序使用的是列名 排序需使用example
        example.setOrderByClause("CREATE_DATE DESC");
        Example.Criteria criteria = example.createCriteria();
        if(CommonUtil.isNotEmpty(sysUser.getOrgCode())){
            criteria.andEqualTo("orgCode", sysUser.getOrgCode());
        }
        if(CommonUtil.isNotEmpty(sysUser.getSexCode())){
            criteria.andEqualTo("sexCode", sysUser.getSexCode());
        }
        if(CommonUtil.isNotEmpty(sysUser.getName())){
            criteria.andLike("name","%"+sysUser.getName()+"%");
        }
        if (CommonUtil.isNotEmpty(sysUser.getIdCard())){
            criteria.andLike("idCard","%"+sysUser.getIdCard()+"%");
        }
        if (CommonUtil.isNotEmpty(sysUser.getPhone())){
            criteria.andLike("phone","%"+sysUser.getPhone()+"%");
        }
        criteria.andNotEqualTo("isSuper", "1");
        PageHelper.startPage(page.getPageNo(), page.getPageSize());
        List<SysUser> list = this.mapper.selectByExampleAndRowBounds(example,null);
        PageInfo pageInfo = new PageInfo(list);
        String total = String.valueOf(pageInfo.getTotal());
        return new DataSet<>(page.getPageNo(), page.getPageSize(), page.getTotalPages(), Integer.parseInt(total), list);
    }

    @Override
    public int save(SysUser sysUser) {
        SysUser old = new SysUser();
        old.setLoginName(sysUser.getLoginName());
        old = mapper.selectOne(old);
        if (null != old){
            throw new CommonException("用户名【"+ sysUser.getLoginName()+"】已经存在，不可重复！");
        }
        /*old = new SysUser();
        old.setPhone(sysUser.getPhone());
        old = mapper.selectOne(old);
        if (old != null){
            throw new CommonException("手机号码【"+ sysUser.getPhone()+"】已经存在，不可重复！");
        }*/
        return this.mapper.insertSelective(sysUser);
    }

    @Override
    public int delete(String id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(SysUser sysUser) {
        return this.mapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public int updateAll(SysUser sysUser) {
        return this.mapper.updateByPrimaryKey(sysUser);
    }

    @Override
    public int deleteByIds(String ids) {
        return mapper.deleteByIds(ids);
    }

    @Override
    public void upadatePassWord(SysUser user) { mapper.updateByPrimaryKeySelective(user);  }

    @Override
    public void updateApiUser(MemberModel member) {
        /*//先删除 再新增
        mapper.deleteByPrimaryKey(member.getId());

        SysUser user = new SysUser();
        user.setUserId(member.getId());
        user.setMember_code(member.getUserCode());
        user.setLoginName(member.getUserName());
        user.setName(member.getUserName());
        //解密 base64密码
        String pwd = Base64Util.decodeBase64(member.getPassword());
        String newPassword = AesEncryptUtil.encrypt(pwd, OauthWebConfig.aceKey, OauthWebConfig.aceVi);
        user.setPassword(newPassword);
        user.setDept_list(member.getDeptCode());
        user.setOrgCode(member.getCorpCode());
        user.setStatus(member.getStatus());
        user.setIsSuper("0");
        user.setSexCode("1");
        user.setCreateDate(new Date());*/

        //mapper.insertSelective(user);

        SysUser user = new SysUser();
        user.setUserId(member.getId());
        user.setStatus(member.getStatus());

        mapper.updateByPrimaryKeySelective(user);

    }

    @Override
    public void updateApiPassword(PasswordModel password) {
        String pwd = Base64Util.decodeBase64(password.getPassword());
        String newPassword = AesEncryptUtil.encrypt(pwd, OauthWebConfig.aceKey, OauthWebConfig.aceVi);

        SysUser user = new SysUser();
        user.setMember_code(password.getUsercode());
        user = mapper.selectOne(user);
        user.setPassword(newPassword);
        mapper.updateByPrimaryKeySelective(user);
    }

    public static void main(String[] args) {
        System.out.println(Base64Util.decodeBase64(""));
    }
}
