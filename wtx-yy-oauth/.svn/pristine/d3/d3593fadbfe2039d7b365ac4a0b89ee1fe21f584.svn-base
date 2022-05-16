package com.kinghis.yyoauth.controller.oauth;

import com.alibaba.fastjson.JSON;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.yyoauth.pojo.SysRolePermission;
import com.kinghis.yyoauth.service.SysRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @DESC: 角色权限控制器
 * @Author: sl
 * @Date: 2019-01-28 13:47
 */
@Controller
@RequestMapping("/oauth/roleper")
public class RolePerController extends WtxBaseController {

    @Autowired
    private SysRolePermissionService sysRolePerService;
    
    /** 
    * @Description: 查询角色权限列表
    * @Author: sl 
    * @Date: 2019-01-31 8:48
    */
    @RequestMapping("list")
    public ModelAndView list(SysRolePermission rolePermission){
        List<SysRolePermission> list = sysRolePerService.queryList(rolePermission);
        return toJson(list);
    }
    
    /** 
    * @Description: 保存角色权限
    * @Author: sl
    * @Date: 2019-01-31 8:48
    */
    @RequestMapping("save")
    public ModelAndView save(String str){
        List<SysRolePermission> list = JSON.parseArray(str, SysRolePermission.class);
        sysRolePerService.batchSave(list);
        return toJson();
    }

}
