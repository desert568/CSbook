package com.kinghis.emri.controller;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.alibaba.fastjson.JSON;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.emri.config.PropertiesConfig;
import com.kinghis.emri.model.EmriModel;
import com.kinghis.emri.pojo.T_emri_error;
import com.kinghis.emri.service.EmrService;
import com.kinghis.emri.service.ErrorService;
import com.kinghis.emri.util.ParamsUtil;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("emr")
public class EmrController extends WtxBaseController {

    @Autowired
    private EmrService emrService;

    @Autowired
    private ErrorService errorService;


    @RequestMapping("collect")
    public ModelAndView operEmr(EmriModel emriModel, HttpServletRequest request){
        String userName = (String) request.getSession().getAttribute("login_name");
        if (CommonUtil.isEmpty(userName)){
            userName = "super";
        }
        //SQL注入参数校验
        ParamsUtil.isSqlValid(emriModel.getPatient_id());
        ParamsUtil.isSqlValid(emriModel.getStart_date());
        ParamsUtil.isSqlValid(emriModel.getEnd_date());
        ParamsUtil.isSqlValid(emriModel.getTable_name());

        emriModel.setUserName(userName);
        long st = System.currentTimeMillis();
        String result ="";
        if("1".equals(PropertiesConfig.emrFlag)) {
            //http
            emrService.operHttp(emriModel);
        }else if("2".equals(PropertiesConfig.emrFlag)) {
            emrService.operWebService(emriModel);
        }else{
            //常规
            result = emrService.operInterface(emriModel);
        }
        long et = System.currentTimeMillis();
        return toJson(result +"</br>总耗时：" + (et - st) / 1000 + "秒");
    }

    /**
    * @Description: 查询错误信息
    * @Author: sl
    * @Date: 2020-09-17 11:01
    */
    @RequestMapping("queryError")
    public ModelAndView queryError(T_emri_error request, Integer pageIndex, Integer pageSize){
        ParamsUtil.isSqlValid(request.getPatient_id());
        ParamsUtil.isSqlValid(request.getStart_date());
        ParamsUtil.isSqlValid(request.getEnd_date());
        DataSet<T_emri_error> data =  errorService.queryError(request,new Page(pageIndex, pageSize));
        return toJson(data);
    }

    /**
    * @Description: 清空错误日志
    * @Author: sl
    * @Date: 2020-11-23 10:58
    */
    @RequestMapping("deleteAll")
    public ModelAndView deleteAll(T_emri_error request){
        errorService.deleteAll(request);
        return toJson();
    }

    /**
    * @Description: 重传
    * @Author: sl
    * @Date: 2020-09-17 13:40
    */
    @RequestMapping("arq")
    public ModelAndView arq(String json, HttpServletRequest request){
        String userName = (String) request.getSession().getAttribute("login_name");
        if (CommonUtil.isEmpty(userName)){
            userName = "super";
        }
        long st = System.currentTimeMillis();
        List<T_emri_error> errorList = JSON.parseArray(json, T_emri_error.class);
        String result ="";
        if("0".equals(PropertiesConfig.emrFlag)) {
            for (T_emri_error error : errorList){
                EmriModel emriModel  = new EmriModel();
                emriModel.setStart_date(error.getStart_date());
                emriModel.setEnd_date(error.getEnd_date());
                emriModel.setPatient_id(error.getPatient_id());
                emriModel.setUserName(userName);
                result += emrService.operInterface(emriModel);
            }
        }else  if("1".equals(PropertiesConfig.emrFlag)) {
            //http
            for (T_emri_error error : errorList){
                EmriModel emriModel  = new EmriModel();
                emriModel.setStart_date(error.getStart_date());
                emriModel.setEnd_date(error.getEnd_date());
                emriModel.setPatient_id(error.getPatient_id());
                emriModel.setUserName(userName);
                result += emrService.operWebService(emriModel);
            }
        }else if("2".equals(PropertiesConfig.emrFlag)) {
            //webService
            for (T_emri_error error : errorList){
                EmriModel emriModel  = new EmriModel();
                emriModel.setStart_date(error.getStart_date());
                emriModel.setEnd_date(error.getEnd_date());
                emriModel.setPatient_id(error.getPatient_id());
                emriModel.setUserName(userName);
                result += emrService.operWebService(emriModel);
            }

        }

        long et = System.currentTimeMillis();
        return toJson(result +"</br>总耗时：" + (et - st) / 1000 + "秒");
    }

    @RequestMapping("toDetail")
    public ModelAndView toDetail(String id){
        T_emri_error error = errorService.getById(id);
        return toView("../admin/detail", error.getSource());
    }
    @RequestMapping("toErrorDetail")
    public ModelAndView toErrorDetail(String id){
        T_emri_error error = errorService.getById(id);
        return toView("../admin/detail", error.getError_msg());
    }




}
