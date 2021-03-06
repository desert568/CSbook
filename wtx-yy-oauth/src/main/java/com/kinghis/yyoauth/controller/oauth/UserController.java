package com.kinghis.yyoauth.controller.oauth;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.common.cache.RedisService;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.common.util.AesEncryptUtil;
import com.kinghis.common.util.IDGenUtil;
import com.kinghis.yyoauth.model.QxUser;
import com.kinghis.yyoauth.model.WtxUserInfo;
import com.kinghis.yyoauth.pojo.*;
import com.kinghis.yyoauth.common.config.OauthWebConfig;
import com.kinghis.yyoauth.model.JsTree;
import com.kinghis.yyoauth.service.SysProjectService;
import com.kinghis.yyoauth.service.SysRoleService;
import com.kinghis.yyoauth.service.SysUserRoleService;
import com.kinghis.yyoauth.service.SysUserService;
import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Desc 用户管理
 * @Date 2018/12/21 10:19
 * @Author liuc
 */
@Controller
@RequestMapping("/oauth/user")
public class UserController extends WtxBaseController {

    private static String SUPER = "super";

    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysProjectService sysProjectService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     *@Desc 根据条件分页查询用户列表
     *@Author ymz
     *@Date 2019/1/31 10:37
     */
    @RequestMapping("queryPage")
    public ModelAndView queryPage(SysUser sysUser, Integer pageIndex, Integer pageSize){
        DataSet<SysUser> data = sysUserService.queryPage(sysUser, new Page(pageIndex,pageSize));
        return toJson(data);
    }
    
    /**
     *@Desc 跳转到用户新增/编辑页面
     *@Author yzm
     *@Date 2019/1/31 10:37
     */
    @RequestMapping("user-add")
    public ModelAndView toAdd(String id){
        SysUser info = new SysUser();
        if (CommonUtil.isNotEmpty(id)){
            info = sysUserService.getSysUser(id);
            //查询时password反AES
            if (CommonUtil.isNotEmpty(info.getPassword())){
                info.setPassword(AesEncryptUtil.desEncrypt(info.getPassword(), OauthWebConfig.aceKey, OauthWebConfig.aceVi));
            }
        }
        return toView("user-add", info);
    }
    /**
     *@Desc 新增用户
     *@Author yzm
     *@Date 2019/1/31 10:37
     */
    @RequestMapping("add")
    public ModelAndView add(SysUser sysUser){
        if (sysUser.getLoginName().equals(SUPER)){
            sysUser.setIsSuper("1");
        } else {
            sysUser.setIsSuper("0");
        }
        sysUser.setUserId(IDGenUtil.getID());
        sysUser.setCreateDate(new Date());
        //设置密码为AES加密
        sysUser.setPassword(AesEncryptUtil.encrypt(sysUser.getPassword(), OauthWebConfig.aceKey, OauthWebConfig.aceVi));
        sysUserService.save(sysUser);
        return toJson();
    }


    /**
     *@Desc 修改密码
     *@Author yzm
     *@Date 2019/2/12 11:37
     */
    @RequestMapping("updatePassword")
    public ModelAndView  updatePassWord(HttpServletRequest request,String oldPass,String newPass){

        //获得当前用户
        SysUser user = WtxUserInfo.getSysUser();

        //加密旧密码
        String oldPassword = AesEncryptUtil.encrypt(oldPass, OauthWebConfig.aceKey, OauthWebConfig.aceVi);
        if(!oldPassword.equals(user.getPassword()))
        {
            throw new CommonException("原密码不对，请重新输入！");
        }
        String newPassword = AesEncryptUtil.encrypt(newPass, OauthWebConfig.aceKey, OauthWebConfig.aceVi);

        user.setPassword(newPassword);
        //调用服务修改密码
        sysUserService.upadatePassWord(user);
        return toJson();

    }


    /**
     *@Desc 更新用户信息
     *@Author yzm
     *@Date  2019/1/31 10:37
     */
    @RequestMapping("update")
    public ModelAndView update(SysUser sysUser){
        //设置密码为AES加密
        sysUser.setPassword(AesEncryptUtil.encrypt(sysUser.getPassword(), OauthWebConfig.aceKey, OauthWebConfig.aceVi));
        sysUserService.update(sysUser);
        return toJson();
    }
    /**
     *@Desc 删除用户信息
     *@Author yzm
     *@Date 2019/1/31 10:37
     */
    @RequestMapping("delete")
    public ModelAndView delete(String ids){
        sysUserService.deleteByIds(ids);
        return toJson();
    }

    /**
     * 获取菜单树
     *@Author yzm
     *@Date 2019/1/31 10:37
     */
    @RequestMapping("menuTree")
    public ModelAndView menuTree(HttpServletRequest request){
        QxUser qxUser = WtxUserInfo.getQxUser();
        Map<String,SysMenu> mapMenu = qxUser.getMenus();
        StringBuffer sb = new StringBuffer();
        for(SysMenu menu:mapMenu.values()){
            //如果是根菜单
            if(CommonUtil.isEmpty(menu.getParentCode()) || "root".equals(menu.getParentCode())){
                String html  = makeMenu(mapMenu,menu);
                sb.append(html);
            }
        }
        return toJson(sb.toString());
    }

    private String makeMenu(Map<String,SysMenu> mapMenu, SysMenu menu){
        StringBuffer sb = new StringBuffer();
        if(CommonUtil.isNotEmpty(menu.getChilds())){
            sb.append("<li>");
            sb.append("<a href=\"#\" class=\"dropdown-toggle\">");
            sb.append("<i class=\""+menu.getIconCode()+"\"></i>");
            sb.append("<span class=\"menu-text\" style=\"padding-left: 5px;\">"+menu.getMenuName()+"</span>");
            sb.append("<b class=\"arrow icon-angle-down\"></b>");
            sb.append("</a>");
            sb.append("<ul class=\"submenu\">");
            List<String> list = menu.getChilds();
            for(String code:list){
                SysMenu child = mapMenu.get(code);
                if(child==null){
                    continue;
                }
                //没有子菜单
                if(CommonUtil.isEmpty(child.getChilds())){
                    sb.append("<li>");
                    sb.append("<a href=\"javascript:addTabs({id:'"+child.getMenuCode()+"',title:'"+child.getMenuName()+"',close: true,url: _basePath + '"+child.getMenuUrl()+"?menuCode="+child.getMenuCode()+"'})\" title=\""+child.getMenuName()+"\">"+child.getMenuName()+"</a>");
					sb.append("</li>");
                }else{
                    //还有子菜单，那么继续递归
                    String html = makeMenu(mapMenu, child);
                    sb.append(html);
                }
            }
            sb.append("</ul>");
            sb.append("</li>");
        }else{

        }
        return sb.toString();
    }


    /** 
    * @Description: 查询当前登录人有权限的项目的所有角色
    * @Author: sl 
    * @Date: 2019-01-29 8:43
    */
    @RequestMapping("user-oauth")
    public ModelAndView oauth(String userId, HttpServletRequest request){
        //先查询所有有权限的项目，以防止项目下无角色时不显示项目
        SysProject project = new SysProject();
        project.setStatus("1");
        String projectCodes = WtxUserInfo.getUserProjectCodes();
        List<SysProject> projectList = sysProjectService.queryList(project, projectCodes);

        SysRole role = new SysRole();
        List<SysRole> roleList = sysRoleService.queryList(role, projectCodes);
        //根据项目编码组装成jstree
        List<JsTree> tree = bindTree(projectList, roleList);

        //查询当前用户已有角色
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        List<SysUserRole> list = sysUserRoleService.queryList(userRole);

        return toView("user-oauth", tree, userId, list);
    }

    private List<JsTree> bindTree(List<SysProject> projectList, List<SysRole> roleList){
        List<JsTree> list = new ArrayList<JsTree>();
        for (SysProject project : projectList){
            JsTree parentTree = new JsTree();
            parentTree.setId(project.getProjectCode());
            parentTree.setText(project.getProjectName());
            List<JsTree> childList = new ArrayList<JsTree>();
            for (SysRole role : roleList){
                if (role.getProjectCode().equals(project.getProjectCode())){
                    JsTree child = new JsTree();
                    child.setId(role.getRoleId());
                    child.setText(role.getRoleName());
                    child.setParent_id(role.getProjectCode());
                    child.setIcon("layui-icon layui-icon-username");
                    childList.add(child);
                }
            }
            parentTree.setChildren(childList);
            list.add(parentTree);
        }
        return list;
    }
}
