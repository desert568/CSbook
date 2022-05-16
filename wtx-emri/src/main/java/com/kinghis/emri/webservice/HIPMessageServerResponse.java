package com.kinghis.emri.webservice;

import javax.xml.bind.annotation.*;


/**
 * <p>anonymous complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="HIPMessageServerResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "hipMessageServerResult"
})
@XmlRootElement(name = "HIPMessageServerResponse")
public class HIPMessageServerResponse {

    @XmlElement(name = "HIPMessageServerResult", required = true)
    protected String hipMessageServerResult;

    /**
     * ��ȡhipMessageServerResult���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getHIPMessageServerResult() {
        return hipMessageServerResult;
    }

    /**
     * ����hipMessageServerResult���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHIPMessageServerResult(String value) {
        this.hipMessageServerResult = value;
    }

}
