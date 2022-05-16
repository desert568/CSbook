package com.kinghis.yyoauth.controller.data;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.yyoauth.model.AreaModel;
import com.kinghis.yyoauth.pojo.SysArea;
import com.kinghis.yyoauth.service.AreaService;
import com.kinghis.yyoauth.service.DeptService;
import com.kinghis.yyoauth.service.DictOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import java.util.Map;


/**
 * @description: 地区管理
 * @author ydd
 * @date 2022-04-12 9:37
 */
@Controller
@RequestMapping("/data/area")
public class AreaController extends WtxBaseController {


    @Autowired
    private AreaService areaService;
    /**
     * @description: 地区查询
     * @author ydd
     * @date 2022-04-12 10:16
     */
    @RequestMapping("queryPage")
    public ModelAndView queryPage(AreaModel sysArea) {
        DataSet<Map<String,Object>> data = areaService.queryPage(sysArea);
        return toJson(data);
    }



}
