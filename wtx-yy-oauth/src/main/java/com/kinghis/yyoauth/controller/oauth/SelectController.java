package com.kinghis.yyoauth.controller.oauth;

import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.common.model.SelectModel;
import com.kinghis.yyoauth.model.WtxUserInfo;
import com.kinghis.yyoauth.pojo.SysProject;
import com.kinghis.yyoauth.service.SysProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @DESC: 下拉框控制器
 * @Author: sl
 * @Date: 2019-01-23 15:38
 */
@Controller
@RequestMapping("/oauth/select")
public class SelectController extends WtxBaseController {

    @Autowired
    private SysProjectService sysProjectService;

    /**
    * @Description: 查询项目列表，并组装成SelectModel返回
    * @Author: sl
    * @Date: 2019-01-31 8:49
    */
    @RequestMapping("listProject")
    public ModelAndView listProject(String projectName, HttpServletRequest request){
        SysProject project = new SysProject();
        project.setStatus("1");
        project.setProjectName(projectName);
        String projectCodes = WtxUserInfo.getUserProjectCodes();
        List<SysProject> list = sysProjectService.queryList(project, projectCodes);
        List<SelectModel> selectModelList = list.parallelStream().map(item ->
                new SelectModel(item.getProjectCode(), item.getProjectName()))
                .collect(Collectors.toList());
        return toJson(selectModelList);
    }
}
