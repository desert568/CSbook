package com.kinghis.emri.controller;

import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.common.model.FormSelectModel;
import com.kinghis.emri.pojo.T_emri_source;
import com.kinghis.emri.pojo.T_emri_sql;
import com.kinghis.emri.service.SQLService;
import com.kinghis.emri.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-10 17:28
 */
@Controller
@RequestMapping("sql")
public class SQLController extends WtxBaseController {

    @Autowired
    private SQLService sqlService;

    @Autowired
    private SourceService sourceService;

    @RequestMapping("queryByTableName")
    public ModelAndView queryPage(String tableName) {
        T_emri_sql data = sqlService.queryByTableName(tableName);
        return toJson(data);
    }

    @RequestMapping("add")
    public ModelAndView add(T_emri_sql model) {
        sqlService.add(model);
        return toJson();
    }

    /**
     * @Description: 查询数据源列表
     * @Author: sl
     * @Date: 2020-09-16 15:32
     */
    @RequestMapping("listSource")
    public ModelAndView listSource() {
        List<T_emri_source> list = sourceService.listSource();
        List<FormSelectModel> formList = list.parallelStream().map(item ->
                        new FormSelectModel(item.getSource_code(), item.getSource_name()))
                .collect(Collectors.toList());
        return toJson(formList);
    }

    /**
     * @Description: 查询所有表
     * @Author: sl
     * @Date: 2020-09-17 9:56
     */
    @RequestMapping("listTable")
    public ModelAndView listTable() {
        List<T_emri_sql> list = sqlService.listTable();
        List<FormSelectModel> formList = list.parallelStream().map(item ->
                        new FormSelectModel(item.getTable_name(), item.getTable_desc()))
                .collect(Collectors.toList());
        return toJson(formList);
    }
}
