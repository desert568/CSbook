package com.kinghis.yyoauth.controller;

import com.kinghis.common.model.LoginToken;
import com.kinghis.common.util.AesEncryptUtil;
import com.kinghis.yyoauth.common.config.OauthWebConfig;
import com.kinghis.yyoauth.model.QxUser;
import com.kinghis.yyoauth.pojo.SysUser;
import com.kinghis.yyoauth.service.LoginService;
import com.kinghis.yyoauth.service.SysUserService;
import com.wtx.common.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

/**
 * @Desc 用户登录
 * @Date 2019/1/23 17:27
 * @Author liuc
 */
@Api(value = "登录", description = "登录模块")
@RestController
@RequestMapping("/loginApi")
public class LoginApiController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录
     * @param
     * @return
     */
    @ApiOperation(value = "获取用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "projectCode", value = "项目编码", required = true,  paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "getQxUser",method = RequestMethod.GET)
    public QxUser getQxUser(String userId, String projectCode){
        QxUser qxUser = this.loginService.getQxUser(userId, projectCode);
        return qxUser;
    }

    @RequestMapping(value = "ssoLogin", method = RequestMethod.POST)
    public Map<String, Object> ssoLogin(@RequestBody LoginToken token){
        Map<String, Object> map = new HashMap<>();
        if(CommonUtil.isEmpty(token.getUsername())){
            map.put("code", 0);
            map.put("msg", "用户名为空");
            return map;
        }
        if(CommonUtil.isEmpty(token.getPassword())){
            map.put("code", 0);
            map.put("msg", "密码为空");
            return map;
        }
        SysUser sysUser = this.loginService.ssoLogin(token);
        if (null == sysUser){
            map.put("code", 0);
            map.put("msg", "用户名与密码不匹配或没有登录该项目的权限");
            return map;
        }
        map.put("code", 1);
        map.put("msg", "登录成功");
        map.put("user", sysUser);
        return map;
    }

    /**
     * @DESC:  根据用户类型查询
     * @Author: liubo
     * @Date: 2020/11/24 下午2:07
     */
    @RequestMapping(value = "queryUserByType", method = RequestMethod.GET)
    public List<SysUser> queryUserByType(String userType){
        SysUser sysUser = new SysUser();
        sysUser.setStatus("1");
        if(CommonUtil.isNotEmpty(userType)) {
            sysUser.setUserType(userType);
        }
        List<SysUser> data = sysUserService.queryList(sysUser);
        return data;
    }

    /**
     * @auther: HuT
     * @Description: 修改密码
     * @date: 2021/5/13 16:34
     */
    @RequestMapping(value = "updatePwd")
    public Integer updatePassWord(@RequestBody SysUser user,String oldPass,String newPass){
        //获得当前用户
        //加密旧密码
        String oldPassword = AesEncryptUtil.encrypt(oldPass, OauthWebConfig.aceKey, OauthWebConfig.aceVi);
        String newPassword = AesEncryptUtil.encrypt(newPass, OauthWebConfig.aceKey, OauthWebConfig.aceVi);
        if(!oldPassword.equals(user.getPassword())){
            return 1;
        }else{
            //调用服务修改密码
            user.setPassword(newPassword);
            sysUserService.upadatePassWord(user);
            return 0;
        }
    }

    /**
    * @Description: 对外提供其他业务系统修改密码功能
    * @Author: sl
    * @Date: 2021-07-16 16:10
    */
    @RequestMapping(value = "updatePassWord", method = RequestMethod.POST)
    public Map updatePassWord(String userId, String oldPass, String newPass){
        Map<String, Object> map = new HashMap<>();
        if (CommonUtil.isEmpty(userId)){
            map.put("code", 0);
            map.put("msg", "用户ID不可为空");
            return map;
        } else if (CommonUtil.isEmpty(oldPass)){
            map.put("code", 0);
            map.put("msg", "原始密码不可为空");
            return map;
        } else if (CommonUtil.isEmpty(newPass)){
            map.put("code", 0);
            map.put("msg", "新密码不可为空");
            return map;
        } else if (oldPass.trim().equals(newPass.trim())){
            map.put("code", 0);
            map.put("msg", "新密码不可与原始密码一致");
            return map;
        }
        //获得当前用户
        SysUser user = sysUserService.getSysUser(userId);
        if (null == user){
            map.put("code", 0);
            map.put("msg", "用户信息不存在");
            return map;
        }
        //加密旧密码
        String oldPassword = AesEncryptUtil.encrypt(oldPass.trim(), OauthWebConfig.aceKey, OauthWebConfig.aceVi);
        String newPassword = AesEncryptUtil.encrypt(newPass.trim(), OauthWebConfig.aceKey, OauthWebConfig.aceVi);
        if(!oldPassword.equals(user.getPassword())){
            map.put("code", 0);
            map.put("msg", "原始密码错误");
            return map;
        } else {
            //调用服务修改密码
            user.setPassword(newPassword);
            sysUserService.upadatePassWord(user);
            map.put("code", 1);
            map.put("msg", "密码修改成功");
            return map;
        }
    }
}
