package com.kinghis.emri.webservice;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by Apache CXF 3.2.10
 * 2020-12-26T14:23:13.933+08:00
 * Generated source version: 3.2.10
 */
@WebServiceClient(name = "PUB0001",
        wsdlLocation = "http://172.26.1.100/csp/hsb/DHC.Published.PUB0001.BS.PUB0001.CLS?WSDL=1",
        targetNamespace = "http://www.dhcc.com.cn")
public class PUB0001 extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.dhcc.com.cn", "PUB0001");
    public final static QName PUB0001Soap = new QName("http://www.dhcc.com.cn", "PUB0001Soap");

    static {
        URL url = null;
        try {
            url = new URL("http://172.26.1.100/csp/hsb/DHC.Published.PUB0001.BS.PUB0001.CLS?WSDL=1");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(PUB0001.class.getName())
                    .log(java.util.logging.Level.INFO,
                            "Can not initialize the default wsdl from {0}", "http://172.26.1.100/csp/hsb/DHC.Published.PUB0001.BS.PUB0001.CLS?WSDL=1");
        }
        WSDL_LOCATION = url;
    }

    public PUB0001(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public PUB0001(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PUB0001() {
        super(WSDL_LOCATION, SERVICE);
    }

    public PUB0001(WebServiceFeature... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public PUB0001(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public PUB0001(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }


    /**
     * @return returns PUB0001Soap
     */
    @WebEndpoint(name = "PUB0001Soap")
    public PUB0001Soap getPUB0001Soap() {
        return super.getPort(PUB0001Soap, PUB0001Soap.class);
    }

    /**
     * @param features A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns PUB0001Soap
     */
    @WebEndpoint(name = "PUB0001Soap")
    public PUB0001Soap getPUB0001Soap(WebServiceFeature... features) {
        return super.getPort(PUB0001Soap, PUB0001Soap.class, features);
    }

}
