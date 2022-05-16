package com.kinghis.yyoauth.controller.oauth;

import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.yyoauth.pojo.SysFunc;
import com.kinghis.yyoauth.service.SysFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 资源管理控制层
 *
 * @author ylfcf
 * @Time 2019-01-29 16:59
 */
@Controller
@RequestMapping("/oauth/func")
public class FuncController extends WtxBaseController {

    @Autowired
    private SysFuncService sysFuncService;

    /*@RequestMapping("menu-index")
    public ModelAndView index() {
        return toView("menu-index");
    }*/

    /*@RequestMapping("list")
    public ModelAndView list(SysFunc sysFunc) {
        return null;
    }*/


    /** 
    * @Description: 保存资源
    * @Author: sl 
    * @Date: 2019-01-31 8:43
    */
    @RequestMapping("save")
    public ModelAndView save(SysFunc sysFunc) {
        this.sysFuncService.save(sysFunc);
        return toJson();
    }

    /**
     * @Description: 修改资源
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("update")
    public ModelAndView update(SysFunc sysFunc) {
        sysFunc.setMenuCode(null);
        this.sysFuncService.update(sysFunc);
        return toJson();
    }

    /**
     * @Description: 删除资源
     * @Author: sl
     * @Date: 2019-01-31 8:43
     */
    @RequestMapping("delete")
    public ModelAndView delete(String id) {
        this.sysFuncService.delete(id);
        return toJson();
    }
}
