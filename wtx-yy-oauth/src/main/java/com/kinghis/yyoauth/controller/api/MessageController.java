package com.kinghis.yyoauth.controller.api;

import com.alibaba.fastjson.JSON;
import com.kinghis.yyoauth.model.api.*;
import com.kinghis.yyoauth.service.DeptService;
import com.kinghis.yyoauth.service.SysUserRoleService;
import com.kinghis.yyoauth.service.SysUserService;
import com.wtx.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2021-11-04 16:31
 */
@RestController
@RequestMapping("/api/message/")
public class MessageController {

    //人员
    private static String THPS_USER = "SYSTEM_THPS_USER";

    //科室
    private static String THPS_DEPT = "SYSTEM_THPS_DEPT";

    //角色
    private static String THPS_ROLE_USER = "SYSTEM_THPS_ROLE_USER";

    //修改密码
    private static String BAS_USER_PWD_MOD = "SYSTEM_BAS_USER_PWD_MOD";


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    private  static Logger logger = LoggerFactory.getLogger(MessageController.class);

    /**
    * @Description: 消息接收 接收集成平台推送的消息
    * @Author: sl
    * @Date: 2021-11-04 16:33
    */
    @RequestMapping("receive")
    public Map<String, Object> receive(@RequestBody String msg){
        logger.info("接收到集成平台推送消息：" + msg);
        Map<String, Object> map = new HashMap<>();
        map.put("object", null);
        map.put("statusCode", 0);
        map.put("success", false);
        if (CommonUtil.isEmpty(msg)){
            map.put("message", "入参为空");
            return map;
        }

        MessageModel model = JSON.parseObject(msg, MessageModel.class);
        logger.info("消息类型为：" + model.getTopicConfig().getTopicCode());
        try {
            if (THPS_USER.equals(model.getTopicConfig().getTopicCode())){
                //获取人员信息
                MemberModel member = JSON.parseObject(model.getMessage().getContent(), MemberModel.class);
                //修改人员信息
                updateMemberInfo(member);
                logger.info("保存推送人员变更信息成功：" + JSON.toJSON(member));
            } else if (THPS_DEPT.equals(model.getTopicConfig().getTopicCode())){
                //获取科室信息
                DeptModel dept = JSON.parseObject(model.getMessage().getContent(), DeptModel.class);
                saveDeptInfo(dept);
                logger.info("保存推送科室变更信息成功：" + JSON.toJSON(dept));
            } else if (THPS_ROLE_USER.equals(model.getTopicConfig().getTopicCode())){
                //获取角色信息
                List<UserRoleModel> userRoleList = JSON.parseArray(model.getMessage().getContent(), UserRoleModel.class);
                for (UserRoleModel userRole : userRoleList){
                    saveUserRole(userRole);
                }
                logger.info("保存推送角色变更信息成功：" + JSON.toJSON(userRoleList));
            } else if (BAS_USER_PWD_MOD.equals(model.getTopicConfig().getTopicCode())){
                //修改密码
                PasswordModel password = JSON.parseObject(model.getMessage().getContent(), PasswordModel.class);
                updatePassword(password);
                logger.info("修改推送密码变更信息成功：" + JSON.toJSON(password));
            } else {
                logger.info("推送主题为：" + model.getTopicConfig().getTopicCode() + "不在操作范围内！");
            }
            map.put("object", model.getMessage().getMessageId());
            map.put("statusCode", 200);
            map.put("success", true);
            map.put("message", null);
        } catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        logger.info("返回集成平台信息：" + JSON.toJSON(map));
        return map;
    }

    private void updateMemberInfo(MemberModel member){
        sysUserService.updateApiUser(member);
    }

    private void saveDeptInfo(DeptModel dept){
        deptService.saveApiDept(dept);
    }

    private void updatePassword(PasswordModel password){
        sysUserService.updateApiPassword(password);
    }

    private void saveUserRole(UserRoleModel userRole){
        sysUserRoleService.saveApiUserRole(userRole);
    }
}
