package com.kinghis.emri.controller;

import com.kinghis.common.controller.WtxBaseController;
import com.kinghis.emri.config.UserEnum;
import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-09 14:58
 */
@Controller
@RequestMapping(value = {"", "/", "/admin"})
public class AdminController extends WtxBaseController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Value("${wtxName}")
    private String userName;

    @Value("${wtxPwd}")
    private String userPwd;

    @RequestMapping(value = {"", "index"})
    public ModelAndView index(HttpServletRequest request) {
        String name = (String) request.getSession().getAttribute("login_name");
        if (CommonUtil.isNotEmpty(name)) {
            return toView("/admin/index");
        }
        return toView("/login/index");
    }

    @RequestMapping("top")
    public ModelAndView top() {
        logger.info("跳转到首页...........");
        logger.error("跳转到首页..........");
        return toView("/admin/top");
    }

    @RequestMapping("login")
    public ModelAndView login(HttpServletRequest request, String name, String password) {
        if (CommonUtil.isEmpty(UserEnum.getName(name)) && !userName.equals(name)) {
            throw new CommonException("用户名不正确");
        }
        if (!userPwd.equals(password)) {
            throw new CommonException("密码不正确");
        }
        request.getSession().setAttribute("login_name", name);
        request.getSession().setMaxInactiveInterval(-1); //不超时
        return toJson();
    }
}
