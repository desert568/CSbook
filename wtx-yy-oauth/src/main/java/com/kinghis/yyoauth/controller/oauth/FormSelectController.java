package com.kinghis.yyoauth.controller.oauth;

import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.common.model.FormSelectChildrenModel;
import com.kinghis.common.model.FormSelectModel;
import com.kinghis.yyoauth.model.WtxUserInfo;
import com.kinghis.yyoauth.pojo.SysMenu;
import com.kinghis.yyoauth.pojo.SysProject;
import com.kinghis.yyoauth.service.SysMenuService;
import com.kinghis.yyoauth.service.SysProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** @Desc 下拉框控制器
  * @author liub
  * @Date 2019/2/27 20:36
  */
@Controller
@RequestMapping("/oauth/formselect")
public class FormSelectController extends WtxBaseController {

    @Autowired
    private SysProjectService sysProjectService;
    @Autowired
    private SysMenuService sysMenuService;

   /** @Desc  查询项目列表，并组装成并组装成FormSelectModel返回返回
     * @author liub
     * @Date 2019/2/27 20:37
     */
    @RequestMapping("listProjectSelect")
    public ModelAndView listProjectSelect(String projectCode){
        String projectCodes = WtxUserInfo.getUserProjectCodes();
        List<SysProject> list = sysProjectService.listProjectSelect(projectCode,projectCodes);
        List<FormSelectModel> formSelectModelList = list.parallelStream().map(item ->
                new FormSelectModel(item.getProjectCode(), item.getProjectCode()+"_"+item.getProjectName()))
                .collect(Collectors.toList());
        return toJson(formSelectModelList);
    }

    /** @Desc  联动查询第一节菜单，并组装成并组装成FormSelectModel返回返回
     * @author liub
     * @Date 2019/2/27 20:37
     */
    @RequestMapping("listMenuSelect")
    public ModelAndView listMenuSelect(){
        SysMenu sysMenu = new SysMenu();
        sysMenu.setStatus("1");
        sysMenu.setParentCode("root");
        sysMenu.setProjectCode("wtx-blacklistweb");
        List<SysMenu> menuList = sysMenuService.queryList(sysMenu);
        List<FormSelectChildrenModel> childrenModelList = new ArrayList<FormSelectChildrenModel>();
        for (SysMenu menu: menuList) {
            FormSelectChildrenModel childrenModel = new FormSelectChildrenModel();
            childrenModel.setValue(menu.getMenuCode());
            childrenModel.setName(menu.getMenuName());
            childrenModel.setChildren(childListMenu(menu.getMenuCode()));
            childrenModelList.add(childrenModel);
        }
        return toJson(childrenModelList);
    }

    /** @Desc   根据上级编码查询下级节点
      * @author liub
      * @Date 2019/2/28 11:22
      */
    private List<FormSelectModel> childListMenu(String pid){
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentCode(pid);
        sysMenu.setStatus("1");
        List<SysMenu> menuList = sysMenuService.queryList(sysMenu);
        List<FormSelectModel> childList = new ArrayList<>();
        for (SysMenu menu: menuList) {
            FormSelectChildrenModel childrenModel = new FormSelectChildrenModel();
            childrenModel.setValue(menu.getMenuCode());
            childrenModel.setName(menu.getMenuName());
            childrenModel.setChildren(childListMenu(menu.getMenuCode()));
            childList.add(childrenModel);
        }
        return childList;
    }
}
