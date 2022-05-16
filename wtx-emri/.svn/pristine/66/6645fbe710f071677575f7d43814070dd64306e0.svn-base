package com.kinghis.emri.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-28 14:24
 */

@Configuration
public class PropertiesConfig {

    public static final String NFS_PATH = System.getProperty("user.dir") + "/sqlFile";

    public static String emrFlag;
    public static String httpUrl;
    public static String orgCode;
    public static String isbasyfjx;

    @Autowired
    public void setEmrFlag(@Value("${emr_flag}") String emrFlag) {
        PropertiesConfig.emrFlag = emrFlag;
    }

    @Autowired
    public void setHttpUrl(@Value("${http_url}") String httpUrl) {
        PropertiesConfig.httpUrl = httpUrl;
    }

    @Autowired
    public void setOrgCode(@Value("${org_code}") String orgCode) {
        PropertiesConfig.orgCode = orgCode;
    }
    @Autowired
    public void setIsbasyfjx(@Value("${isbasyfjx}") String isbasyfjx) {
        PropertiesConfig.isbasyfjx = isbasyfjx;
    }


}
