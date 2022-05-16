package com.kinghis.emri.controller;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.emri.pojo.T_emri_source;
import com.kinghis.emri.service.SourceService;
import com.kinghis.emri.util.ParamsUtil;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-10 9:33
 */
@Controller
@RequestMapping("source")
public class SourceController extends WtxBaseController {

    @Autowired
    private SourceService sourceService;

    @RequestMapping("queryPage")
    public ModelAndView queryPage(T_emri_source request, Integer pageIndex, Integer pageSize){
        ParamsUtil.isSqlValid(request.getSource_name());
        DataSet<T_emri_source> data =  sourceService.queryPage(request,new Page(pageIndex, pageSize));
        return toJson(data);
    }

    @RequestMapping("toAdd")
    public ModelAndView toAdd(String source_code, String see) {
        T_emri_source source = new T_emri_source();
        if (CommonUtil.isNotEmpty(source_code)) {
            source = sourceService.getById(source_code);
        }
        return toView("add", source, see);
    }


    @RequestMapping("add")
    public ModelAndView add(T_emri_source model) {
        sourceService.add(model);
        return toJson();
    }

    @RequestMapping("delete")
    public ModelAndView delete(String ids) {
        sourceService.delete(ids);
        return toJson();
    }

    @RequestMapping("testConnect")
    public ModelAndView testConnect(T_emri_source model) throws SQLException {
        return toJson(sourceService.testConnect(model));
    }
}
