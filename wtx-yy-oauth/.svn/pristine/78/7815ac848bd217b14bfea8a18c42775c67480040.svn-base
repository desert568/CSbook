package com.kinghis.yyoauth.controller.data;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.yyoauth.pojo.sys_hosp;
import com.kinghis.yyoauth.service.HospService;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @DESC: 机构管理
 * @Author: sl
 * @Date: 2019-01-24 16:29
 */
@Controller
@RequestMapping("/data/hosp")
public class HospController extends WtxBaseController {

    @Autowired
    private HospService hospService;




    @RequestMapping("queryPage")
    public ModelAndView queryPage(sys_hosp sys_hosp, int pageIndex, int pageSize) {
        DataSet<Map<String,Object>> data = hospService.queryPage(sys_hosp, new Page(pageIndex, pageSize));
        return toJson(data);
    }


    @RequestMapping("hosp-add")
    public ModelAndView toAdd(String id) {
        sys_hosp info = new sys_hosp();
        if (CommonUtil.isNotEmpty(id)) {
            info = hospService.getHospById(id);
        }
        return toView("hosp-add", info);
    }

    @RequestMapping("add")
    public ModelAndView add(sys_hosp sys_hosp) {
        hospService.save(sys_hosp);
        return toJson();
    }

    @RequestMapping("update")
    public ModelAndView update(sys_hosp sys_hosp) {
        hospService.update(sys_hosp);
        return toJson();
    }

    @RequestMapping("del")
    public ModelAndView del(String ids) {
        hospService.deleteByIds(ids);
        return toJson();
    }

}
