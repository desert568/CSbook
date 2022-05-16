package com.kinghis.yyoauth.controller.oauth;

import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.yyoauth.pojo.SysFunc;
import com.kinghis.yyoauth.pojo.SysMenu;
import com.kinghis.yyoauth.pojo.SysRolePermission;
import com.kinghis.yyoauth.model.JsTree;
import com.kinghis.yyoauth.service.SysFuncService;
import com.kinghis.yyoauth.service.SysMenuService;
import com.kinghis.yyoauth.service.SysRolePermissionService;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @DESC: 菜单控制器
 * @Author: sl
 * @Date: 2019-01-24 10:34
 */
@Controller
@RequestMapping("/oauth/menu")
public class MenuController extends WtxBaseController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysFuncService sysFuncService;

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    /**
     * @Description: 跳转到菜单index页面
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("menu-index")
    public ModelAndView index(){
        return toView("menu-index");
    }

    /**
     * @Description: 查询菜单列表，并封装成JStree
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("list")
    public ModelAndView list(SysMenu sysMenu){
        sysMenu.setStatus("1");
        List<SysMenu> menuList = sysMenuService.queryList(sysMenu);
        List<JsTree> result =  installJsTree(menuList);
        /**
         *封装按钮
         */
        SysFunc sysFunc = new SysFunc();
        sysFunc.setProjectCode(sysMenu.getProjectCode());
        List<SysFunc> funcList = sysFuncService.queryList(sysFunc);
        //组装按钮
        this.installButtonTree(funcList ,result);
        return toJson(result);
    }

    /**
     * @Description: 通过ID查询菜单
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("getSysMenu")
    public ModelAndView getSysMenu(String id){
        SysMenu menu = sysMenuService.getSysMenu(id);
        return toJson(menu);
    }

    /**
     * @Description: 添加菜单
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("add")
    public ModelAndView add(SysMenu sysMenu){
        sysMenu.setUpdateDate(new Date());
        sysMenuService.save(sysMenu);
        return toJson();
    }

    /**
     * @Description: 修改菜单
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("update")
    public ModelAndView update(SysMenu sysMenu){
        sysMenu.setUpdateDate(new Date());
        sysMenuService.update(sysMenu);
        return toJson();
    }

    /**
     * @Description: 删除菜单
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("del")
    public ModelAndView del(String id){
        sysMenuService.delete(id);
        return toJson();
    }

    /**
     * @Description: 根据项目角色查询该角色下所拥有的菜单
     * @Author: sl
     * @Date: 2019-01-29 9:09
     */
    @RequestMapping("listMenuByRole")
    public ModelAndView listMenuByRole(SysRolePermission permission){
        List<SysMenu> menuList = sysMenuService.queryListByRole(permission);
        List<JsTree> result =  installJsTree(menuList);
        //组装角色有权限的按钮
        this.installRoleFunc(permission, result);
        return toJson(result);
    }

    /** 
    * @Description: 根据角色所拥有的权限获取按钮
    * @Author: sl 
    * @Date: 2019-02-14 14:54
    */
    private void installRoleFunc(SysRolePermission permission, List<JsTree> result) {
        List<SysRolePermission> listPer = sysRolePermissionService.queryList(permission);
        //找出角色对应的按钮编码
        String funcCodes = "";
        for (SysRolePermission per : listPer){
            if (CommonUtil.isNotEmpty(per.getFuncCode())){
                funcCodes += per.getFuncCode() + ",";
            }
        }
        if (CommonUtil.isNotEmpty(funcCodes)){
            funcCodes = funcCodes.substring(0, funcCodes.length() - 1);
        }
        SysFunc sysFunc = new SysFunc();
        sysFunc.setProjectCode(permission.getProjectCode());
        List<SysFunc> funcList = sysFuncService.queryListInCodes(sysFunc, funcCodes);

        //组装按钮
        this.installButtonTree(funcList ,result);
    }

    /**
     * @Description: 组装JStree
     * @Author: sl
     * @Date: 2019-01-29 9:31
     */
    private List<JsTree> installJsTree(List<SysMenu> menuList){
        //转换成jstree结构
        List<JsTree> jsTree = new ArrayList<JsTree>();
        for (SysMenu entity : menuList){
            JsTree jstree = new JsTree();
            jstree.setId(entity.getMenuCode());
            jstree.setText(entity.getMenuName());
            jstree.setParent_id(entity.getParentCode());
            jstree.setUrl(entity.getMenuUrl());
            jsTree.add(jstree);
        }

        //定义最终返回jstree
        List<JsTree> result = new ArrayList<JsTree>();

        //封装菜单
        //首先查找出一级菜单 及parent_id 为 root的
        for (JsTree tree : jsTree){
            if ("root".equals(tree.getParent_id())){
                tree.setType("0");
                result.add(tree);
            }
        }

        //封装子菜单
        for (JsTree tree : result){
            tree.setChildren(getChild(tree.getId(), jsTree));
        }
        return result;
    }

    private List<JsTree> getChild(String id, List<JsTree> jsTree){
        //定义子菜单
        List<JsTree> child = new ArrayList<JsTree>();

        //封装第一层子菜单
        for (JsTree tree : jsTree){
            if (id.equals(tree.getParent_id())){
                child.add(tree);
            }
        }
        //递归遍历子菜单
        for (JsTree c : child){
            if (CommonUtil.isEmpty(c.getUrl())){
                c.setChildren(getChild(c.getId(), jsTree));
            }
        }
        //递归退出条件
        if (CommonUtil.isEmpty(child)){
            return null;
        }
        return child;
    }

    /**
     * @Description: 组装按钮
     * @Author: ylfcf
     * @Date: 2019-01-30 10:50
     */
    private void installButtonTree(List<SysFunc> funcList ,List<JsTree> result){


        //按钮分组封装
        Map<String,List<JsTree>> buttonGroupMap = new HashMap<>(16);
        for (SysFunc func : funcList) {
            JsTree jstree = new JsTree();
            jstree.setId(func.getFuncCode());
            jstree.setText(func.getFuncName());
            jstree.setParent_id(func.getMenuCode());
            jstree.setType("3");
            jstree.setIcon("layui-icon layui-icon-component");
            List<JsTree> buttonGroupList = buttonGroupMap.get(func.getMenuCode());
            if(CommonUtil.isEmpty(buttonGroupList)){
                buttonGroupList = new ArrayList<>();
            }
            buttonGroupList.add(jstree);
            buttonGroupMap.put(func.getMenuCode(),buttonGroupList);
        }
        //按钮与菜单关联
        for (JsTree tree : result) {
            this.setButtonChild(tree,buttonGroupMap);
        }
    }

    /**
     * 
     * 按钮与菜单关联递归
     * @author ylfcf
     * @Time 2019-01-30 10:48
     */
    private void setButtonChild(JsTree tree, Map<String,List<JsTree>> buttonGroupMap){
        List<JsTree> buttonGroupList = buttonGroupMap.get(tree.getId());
        if(CommonUtil.isNotEmpty(buttonGroupList)){
            tree.setChildren(buttonGroupList);
        }

        List<JsTree> child = tree.getChildren();
        if(CommonUtil.isEmpty(child)){
            return;
        }

        for (JsTree childTree : child) {
            this.setButtonChild(childTree,buttonGroupMap);
        }
    }

}
