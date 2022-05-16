package com.kinghis.yyoauth.controller.oauth;

import cn.trasen.BootComm.model.DataSet;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.yyoauth.model.WtxUserInfo;
import com.kinghis.yyoauth.pojo.SysProject;
import com.kinghis.yyoauth.service.SysProjectService;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * @Desc 项目管理
 * @Author sl
 * @Date 2019/1/30 8:37
 */
@RequestMapping("/oauth/project")
@Controller
public class ProjectController extends WtxBaseController {

    @Autowired
    private SysProjectService sysProjectService;


    /**
      *@Desc 根据条件分页查询列表
      *@Author sl
      *@Date 2019/1/30 8:37
      */
    @RequestMapping("queryPage")
    public ModelAndView queryPage(SysProject sysProject, Integer pageIndex, Integer pageSize){
        String projectCodes = WtxUserInfo.getUserProjectCodes();
        DataSet<SysProject> data = sysProjectService.queryPage(sysProject, new Page(pageIndex,pageSize), projectCodes);
        return toJson(data);
    }

    /**
     *@Desc 跳转到项目新增/编辑页面
     *@Author sl
     *@Date 2019/1/30 8:37
     */
    @RequestMapping("project-add")
    public ModelAndView toAdd(String id){
        SysProject info = new SysProject();
        if (CommonUtil.isNotEmpty(id)){
            info = sysProjectService.getSysProject(id);
        }
        return toView("project-add", info);
    }

    /**
      *@Desc 新增项目
      *@Author sl
      *@Date 2019/1/30 8:39
      */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(SysProject sysProject){
        sysProject.setCreateDate(new Date());
        sysProjectService.save(sysProject);
        return toJson();
    }

    /**
     *@Desc 更新项目信息
     *@Author sl
     *@Date 2019/1/30 8:39
     */
    @RequestMapping("update")
    public ModelAndView update(SysProject sysProject){
        sysProjectService.update(sysProject);
        return toJson();
    }

    /**
     *@Desc 删除项目信息
     *@Author sl
     *@Date 2019/1/30 8:39
     */
    @RequestMapping("del")
    public ModelAndView del(String ids){
        sysProjectService.deleteByIds(ids);
        return toJson();
    }
}
