package com.kinghis.yyoauth.controller.data;

import cn.trasen.BootComm.model.DataSet;
import cn.trasen.core.feature.orm.mybatis.Page;
import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.yyoauth.pojo.T_ftp_info;
import com.kinghis.yyoauth.service.FtpService;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2021-01-20 15:47
 */
@Controller
@RequestMapping("/data/ftp")
public class FtpController extends WtxBaseController {

    @Autowired
    private FtpService ftpService;

    @RequestMapping("queryPage")
    public ModelAndView queryPage(T_ftp_info ftp, int pageIndex, int pageSize) {
        DataSet<T_ftp_info> data = ftpService.queryPage(ftp, new Page(pageIndex, pageSize));
        return toJson(data);
    }

    @RequestMapping("toAdd")
    public ModelAndView toAdd(String id) {
        T_ftp_info info = new T_ftp_info();
        if (CommonUtil.isNotEmpty(id)) {
            info = ftpService.getFtpInfo(id);
        }
        return toView("add", info);
    }

    @RequestMapping("add")
    public ModelAndView add(T_ftp_info ftp) {
        ftpService.save(ftp);
        return toJson();
    }

    @RequestMapping("update")
    public ModelAndView update(T_ftp_info ftp) {
        ftpService.update(ftp);
        return toJson();
    }

    @RequestMapping("del")
    public ModelAndView del(String ids) {
        ftpService.deleteByIds(ids);
        return toJson();
    }
}
