package com.kinghis.emri.webservice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Classname HIPMessageServer_Service
 * @Description TODO
 * @Date 2020-12-26 14:44
 * @Created by Sunny
 */
public class WebService {
    private static final Logger log = LoggerFactory.getLogger(WebService.class);


    public static String sendWebService(String method, String param) {
        log.info("开始调用SendWebService 接口名称：" + method + "参数：" + param);
        PUB0001 service2 = new PUB0001();
        PUB0001Soap serviceSoap = service2.getPUB0001Soap();
        String rest = serviceSoap.hipMessageServer(method, param);
        log.info("调用SendWebService结束,接口名称：" + method + ";");
        return rest;
    }

    public static void main(String[] args) {
        PUB0001 service2 = new PUB0001();
        PUB0001Soap serviceSoap = service2.getPUB0001Soap();
        String rest = serviceSoap.hipMessageServer("", "");

    }
}
