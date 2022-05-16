package com.kinghis.yyoauth.controller.oauth;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.yyoauth.model.WtxUserInfo;
import com.kinghis.yyoauth.pojo.SysRole;
import com.kinghis.yyoauth.service.SysRoleService;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * @DESC: 角色控制器
 * @Author: sl
 * @Date: 2019-01-23 15:20
 */
@Controller
@RequestMapping("/oauth/role")
public class RoleController extends WtxBaseController {

    @Autowired
    private SysRoleService sysRoleService;

    /*@RequestMapping("role-list")
    public ModelAndView index(){
        return toView("role-list");
    }*/

    /**
     * @Description: 分页查询角色
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("queryPage")
    public ModelAndView queryPage(SysRole sysRole, Integer pageIndex, Integer pageSize){
        String projectCodes = WtxUserInfo.getUserProjectCodes();
        DataSet<SysRole> data = sysRoleService.queryPage(sysRole, new Page(pageIndex,pageSize), projectCodes);
        return toJson(data);
    }

    /**
     * @Description: 跳转到角色添加页面，如果ID为真则为编辑页面
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("role-add")
    public ModelAndView toAdd(String id){
        SysRole info = new SysRole();
        if (CommonUtil.isNotEmpty(id)){
            info = sysRoleService.getSysRole(id);
        }
        return toView("role-add", info);
    }

    /**
     * @Description: 添加角色
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("add")
    public ModelAndView add(SysRole sysRole){
        sysRole.setUpdateDate(new Date());
        sysRoleService.save(sysRole);
        return toJson();
    }

    /**
     * @Description: 修改角色
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("update")
    public ModelAndView update(SysRole sysRole){
        sysRole.setUpdateDate(new Date());
        sysRoleService.update(sysRole);
        return toJson();
    }

    /**
     * @Description: 删除角色
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("del")
    public ModelAndView del(String ids){
        sysRoleService.deleteByIds(ids);
        return toJson();
    }
}
