package com.kinghis.yyoauth.service.impl;

import com.kinghis.common.model.LoginToken;
import com.kinghis.common.util.AesEncryptUtil;
import com.kinghis.yyoauth.common.config.OauthWebConfig;
import com.kinghis.yyoauth.dao.*;
import com.kinghis.yyoauth.model.QxUser;
import com.kinghis.yyoauth.pojo.*;
import com.kinghis.yyoauth.service.LoginService;
import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Desc
 * @Date 2019/1/23 17:24
 * @Author liuc
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysProjectMapper sysProjectMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private SysFuncMapper sysFuncMapper;


    @Override
    public QxUser getQxUser(String userId, String projectCode) {
        //1/查询用户
        SysUser sysUser = new SysUser();
        sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if (null == sysUser) {
            sysUser = new SysUser();
            sysUser.setMember_code(userId);
            sysUser = sysUserMapper.selectOne(sysUser);
        }
        if (null == sysUser){
            return null;
        }

        userId = sysUser.getUserId();

        //2、查询用户项目权限
        List<SysProject> projectList = this.sysProjectMapper.getProjectByUser(userId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        if (CommonUtil.isNotEmpty(projectCode)){
            params.put("projectCode", projectCode);
        }


        //3、查询用户菜单权限
        List<SysMenu> menuList = null;
        //如果是超级用户，加载所有菜单
        if ("1".equals(sysUser.getIsSuper())) {
            SysMenu menup = new SysMenu();
            menup.setStatus("1");
            if (CommonUtil.isNotEmpty(projectCode)){
                menup.setProjectCode(projectCode);
            }
            menuList = this.sysMenuMapper.select(menup);
        } else {
            menuList = this.sysMenuMapper.getMenusByUser(params);
        }

        //4、查询用户按钮权限
        List<SysFunc> funcList = null;
        if ("1".equals(sysUser.getIsSuper())) {
            SysFunc func = new SysFunc();
            if (CommonUtil.isNotEmpty(projectCode)){
                func.setProjectCode(projectCode);
            }
            funcList = sysFuncMapper.select(func);
        } else {
            funcList = this.sysFuncMapper.getFunsByUser(params);
        }

        Map<String, SysRole> roles = new HashMap<>();
        List<SysRole> role_list = null;
        if ("1".equals(sysUser.getIsSuper())) {
            userId = "";
        }
        role_list = this.sysRoleMapper.queryRoleList(userId, projectCode);

        for (SysRole role :role_list){
            roles.put(role.getRoleId(), role);
        }

        QxUser qxUser = new QxUser();
        qxUser.setSysUser(sysUser);
        qxUser.setProjects(projectList);
        qxUser.setRoles(roles);
        qxUser.setMenus(initReturnMenu(menuList));//封装权限菜单
        qxUser.setFuncs(initReturnFunc(funcList));//封装权限按钮
        return qxUser;
    }

    @Override
    public SysUser ssoLogin(LoginToken token) {
        //密码加密
        token.setPassword(AesEncryptUtil.encrypt(token.getPassword(), OauthWebConfig.aceKey, OauthWebConfig.aceVi));
        return this.sysUserMapper.login(token);
    }

    /**
     * 对菜单列表做处理
     * 1、对所有菜单list集合做升序
     * 2、将list转换成map，菜单编码作为key，会过滤掉重复的菜单编码
     * 3、遍历map，找到每个菜单的子菜单集合，并生成一个Map<String,List<SysMenu>> maplis
     * 4、将每个菜单的子菜单集合做升序，并封装到map中
     *
     * @param list
     * @return
     */
    private Map<String, SysMenu> initReturnMenu(List<SysMenu> list) {
        if (list == null) {
            list = new ArrayList<SysMenu>();
        }
        //1、对所有菜单list集合做升序
        Collections.sort(list, sysMenuComparator);
        //2、将list转换成map，菜单编码作为key，会过滤掉重复的菜单编码
        Map<String, SysMenu> map = new LinkedHashMap<String, SysMenu>();
        for (SysMenu m : list) {
            if (map.containsKey(m.getMenuCode())) {
                continue;
            }
            map.put(m.getMenuCode(), m);
        }
        //3、遍历map，找到每个菜单的子菜单集合，并生成一个Map<String,List<SysMenu>> maplist
        Map<String, List<SysMenu>> maplist = new HashMap<String, List<SysMenu>>();
        for (SysMenu m : map.values()) {
            if (CommonUtil.isNotEmpty(m.getParentCode())) {
                SysMenu parent = map.get(m.getParentCode());
                if (parent != null) {
                    List<SysMenu> children = maplist.get(m.getParentCode());
                    if (children == null) {
                        children = new ArrayList<SysMenu>();
                        maplist.put(m.getParentCode(), children);
                    }
                    children.add(m);
                }
            }
        }
        //4、将每个菜单的子菜单集合做升序，并封装到map中
        for (Map.Entry<String, List<SysMenu>> en : maplist.entrySet()) {
            SysMenu sys_menu = map.get(en.getKey());
            if (sys_menu == null) {
                continue;
            }
            List<SysMenu> children = en.getValue();
            //排序
            Collections.sort(children, sysMenuComparator);
            List<String> childs = new ArrayList<String>();
            for (SysMenu menu : children) {
                childs.add(menu.getMenuCode());
            }
            sys_menu.setChilds(childs);
        }
        return map;
    }

    /**
     * 菜单排序，升序
     */
    private Comparator<SysMenu> sysMenuComparator = new Comparator<SysMenu>() {
        @Override
        public int compare(SysMenu o1, SysMenu o2) {
            Integer o1no = 0, o2no = 0;
            try {
                o1no = Integer.parseInt(o1.getMenuNo());
            } catch (Exception e) {
            }
            try {
                o2no = Integer.parseInt(o2.getMenuNo());
            } catch (Exception e) {
            }
            return o1no.compareTo(o2no);
        }
    };


    /**
     * 对权限按钮做处理，转换成map(菜单-按钮集合)
     *
     * @param list
     * @return
     */
    private Map<String, Map<String, SysFunc>> initReturnFunc(List<SysFunc> list) {
        Map<String, Map<String, SysFunc>> menuMap = new HashMap<String, Map<String, SysFunc>>();
        Map<String, SysFunc> funcMap = null;
        for (SysFunc func : list) {
            if (menuMap.containsKey(func.getMenuCode())) {
                funcMap = menuMap.get(func.getMenuCode());
            } else {
                funcMap = new HashMap<String, SysFunc>();
                menuMap.put(func.getMenuCode(), funcMap);
            }
            funcMap.put(func.getFuncCode(), func);
        }
        return menuMap;
    }
}
