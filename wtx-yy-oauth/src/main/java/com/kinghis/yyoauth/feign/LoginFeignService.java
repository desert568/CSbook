package com.kinghis.yyoauth.feign;

import com.kinghis.common.model.LoginToken;
import com.kinghis.yyoauth.model.QxUser;
import com.kinghis.yyoauth.pojo.SysUser;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wtx-yy-oauth")
public interface LoginFeignService {

    @RequestMapping(value = "loginApi/getQxUser")
    QxUser getQxUser(@RequestParam("userId") String userId, @RequestParam("projectCode") String projectCode);

    @RequestMapping(value = "loginApi/ssoLogin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SysUser ssoLogin(LoginToken token);

    @RequestMapping(value = "loginApi/updatePwd", consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer updatePassWord(SysUser user,@RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass);
}
