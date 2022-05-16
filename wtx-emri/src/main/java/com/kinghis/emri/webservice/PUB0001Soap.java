package com.kinghis.emri.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.2.10
 * 2020-12-26T14:23:13.883+08:00
 * Generated source version: 3.2.10
 */
@WebService(targetNamespace = "http://www.dhcc.com.cn", name = "PUB0001Soap")
@XmlSeeAlso({ObjectFactory.class})
public interface PUB0001Soap {
    @WebMethod(operationName = "HIPMessageServer", action = "http://www.dhcc.com.cn/DHC.Published.PUB0001.BS.PUB0001.HIPMessageServer")
    @RequestWrapper(localName = "HIPMessageServer", targetNamespace = "http://www.dhcc.com.cn", className = "com.kinghis.emri.webservice.HIPMessageServer")
    @ResponseWrapper(localName = "HIPMessageServerResponse", targetNamespace = "http://www.dhcc.com.cn", className = "com.kinghis.emri.webservice.HIPMessageServerResponse")
    @WebResult(name = "HIPMessageServerResult", targetNamespace = "http://www.dhcc.com.cn")
    String hipMessageServer(
            @WebParam(name = "input1", targetNamespace = "http://www.dhcc.com.cn")
                    String input1,
            @WebParam(name = "input2", targetNamespace = "http://www.dhcc.com.cn")
                    String input2
    );
}
