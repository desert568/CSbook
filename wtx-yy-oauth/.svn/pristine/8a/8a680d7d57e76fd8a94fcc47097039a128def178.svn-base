package com.kinghis.yyoauth.controller.data;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.common.model.FormSelectModel;
import com.kinghis.yyoauth.pojo.Dict_dept;
import com.kinghis.yyoauth.pojo.T_jxkh_dept;
import com.kinghis.yyoauth.service.DeptService;
import com.kinghis.yyoauth.service.DictOrganizationService;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @DESC: 科室管理
 * @Author: yzm
 * @Date: 2019-01-24 16:29
 */
@Controller
@RequestMapping("/data/dept")
public class DeptController extends WtxBaseController {

    @Autowired
    private DictOrganizationService dictOrganizationService;

    @Autowired
    private DeptService deptService;

    /**
     * @Description: 科室管理查询列表
     * @Author: yzm
     * @Date: 2020/3/24 14:23
     */
    @RequestMapping("queryPageBytj")
    public ModelAndView queryPage(T_jxkh_dept T_jxkh_dept, Integer pageIndex, Integer pageSize) {
        DataSet<Map<String,Object>> data = deptService.queryPage(T_jxkh_dept, new Page(pageIndex, pageSize));
        return toJson(data);
    }


    /**
     * @Description: 跳转科室新增页面
     * @Author: yzm
     * @Date: 2020/3/24 15:23
     */
    @RequestMapping("dept-add")
    public ModelAndView toAdd(String id) {
        Dict_dept T_jxkh_dept = new Dict_dept();
        if (CommonUtil.isNotEmpty(id)) {
            T_jxkh_dept = deptService.getDeptById(id);
        }
        return toView("dept-add", T_jxkh_dept);
    }

    /**
     * @Description: 新增科室
     * @Author: yzm
     * @Date: 2020/3/24 15:26
     */
    @RequestMapping("add")
    public ModelAndView add( Dict_dept dict_dept) {
        deptService.save(dict_dept);
        return toJson();
    }

    /**
     * @Description: 修改科室
     * @Author: yzm
     * @Date: 2020/3/24 15:25
     */
    @RequestMapping("update")
    public ModelAndView update(Dict_dept dict_dept) {
        deptService.update(dict_dept);
        return toJson();
    }

    /**
     * @Description: 单次或批量删除
     * @Author: yzm
     * @Date: 2020/3/24 15:25
     */
    @RequestMapping("del")
    public ModelAndView del(String ids) {
        deptService.deleteByIds(ids);
        return toJson();
    }

    @RequestMapping("listDeptSelect")
    public ModelAndView listDeptSelect(String org_code) {
        List<T_jxkh_dept> list = deptService.listDeptSelect(org_code);
        List<FormSelectModel> formSelectModelList = list.parallelStream().map(item ->
                new FormSelectModel(item.getDept_code(), item.getDept_name()))
                .collect(Collectors.toList());
        return toJson(formSelectModelList);
    }
}
