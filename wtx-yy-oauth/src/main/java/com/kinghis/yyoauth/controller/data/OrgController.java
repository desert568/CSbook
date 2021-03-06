package com.kinghis.yyoauth.controller.data;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.common.model.FormSelectModel;
import com.kinghis.common.model.SelectModel;
import com.kinghis.yyoauth.pojo.DictOrganization;
import com.kinghis.yyoauth.pojo.Dict_dept;
import com.kinghis.yyoauth.pojo.sys_hosp;
import com.kinghis.yyoauth.service.DictOrganizationService;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @DESC: 机构管理
 * @Author: sl
 * @Date: 2019-01-24 16:29
 */
@Controller
@RequestMapping("/data/org")
public class OrgController extends WtxBaseController {

    @Autowired
    private DictOrganizationService dictOrganizationService;

    /*@RequestMapping("org-index")
    public ModelAndView index(){
        return toView("org-list");
    }*/


    @RequestMapping("queryPage")
    public ModelAndView queryPage(DictOrganization dictOrganization, int pageIndex, int pageSize) {
        DataSet<DictOrganization> data = dictOrganizationService.queryPage(dictOrganization, new Page(pageIndex, pageSize));
        return toJson(data);
    }

    /**
     * @Description: 机构列表下拉框查询
     * @Author: cp
     * @Date: 2019/9/12 9:57
     */
    @RequestMapping("listFormSelect")
    public ModelAndView listFormSelect(String orgCode) {
        List<DictOrganization> list = dictOrganizationService.listFormSelect(orgCode);
        List<SelectModel> formSelectModels = list.parallelStream().map(item ->
                new SelectModel(item.getOrgCode(), item.getOrgName()))
                .collect(Collectors.toList());
        return toJson(formSelectModels);
    }

    /**
     * @DESC: 科室下拉框查询
     * @Author: cp
     * @Date: 2019/11/4 15:51
     */
    @RequestMapping("listDeptSelect")
    public ModelAndView listDeptSelect() {
        List<Dict_dept> list = dictOrganizationService.listDeptSelect();
        List<FormSelectModel> formSelectModelList = list.parallelStream().map(item ->
                new FormSelectModel(item.getDecode(), item.getStandard_name()))
                .collect(Collectors.toList());
        return toJson(formSelectModelList);
    }

    /**
     * @DESC: 院区下拉框查询
     * @Author: gq
     * @Date: 2019/11/4 15:51
     */
    @RequestMapping("listHospSelect")
    public ModelAndView listHospSelect(String org_code) {
        List<sys_hosp> list = dictOrganizationService.listHospSelect(org_code);
        List<FormSelectModel> formSelectModelList = list.parallelStream().map(item ->
                new FormSelectModel(item.getHosp_code(), item.getHosp_name()))
                .collect(Collectors.toList());
        return toJson(formSelectModelList);
    }

    @RequestMapping("org-add")
    public ModelAndView toAdd(String id) {
        DictOrganization info = new DictOrganization();
        String a="";
        if (CommonUtil.isNotEmpty(id)) {
            info = dictOrganizationService.getDictOrganization(id);
            if (!ObjectUtils.isEmpty(info.getAreaCode())){
                String[] string1 = info.getAreaCode().split("/");
                a=string1[0];
            }
        }
        return toView("org-add", info, a);
    }

    @RequestMapping("add")
    public ModelAndView add(DictOrganization dictOrganization) {

        dictOrganizationService.save(dictOrganization);
        return toJson();
    }

    @RequestMapping("update")
    public ModelAndView update(DictOrganization dictOrganization) {
        dictOrganizationService.update(dictOrganization);
        return toJson();
    }

    @RequestMapping("del")
    public ModelAndView del(String ids) {
        dictOrganizationService.deleteByIds(ids);
        return toJson();
    }

    /**
     * @Description: 地区下拉列表
     * @Author: cp
     * @Date: 2019/9/12 9:57
     */
    @RequestMapping("listEreaSelect")
    public ModelAndView listEreaSelect(String code) {

        return toJson(dictOrganizationService.listAreaSelect(code));
    }

}
