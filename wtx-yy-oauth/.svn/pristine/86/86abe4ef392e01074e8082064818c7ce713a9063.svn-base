package com.kinghis.yyoauth.controller.oauth;

import com.alibaba.fastjson.JSON;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.yyoauth.pojo.SysUserRole;
import com.kinghis.yyoauth.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @DESC: 用户角色
 * @Author: sl
 * @Date: 2019-01-29 13:49
 */
@RequestMapping("/oauth/userRole")
@Controller
public class UserRoleController extends WtxBaseController {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /** 
    * @Description: 保存用户角色信息
    * @Author: sl 
    * @Date: 2019-01-29 13:54
    */
    @RequestMapping("save")
    public ModelAndView save(String str){
        List<SysUserRole> list = JSON.parseArray(str, SysUserRole.class);
        sysUserRoleService.batchSave(list);
        return toJson();
    }
}
