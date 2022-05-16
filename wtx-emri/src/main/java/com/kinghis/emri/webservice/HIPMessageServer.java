package com.kinghis.emri.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="input1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="input2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "input1",
        "input2"
})
@XmlRootElement(name = "HIPMessageServer")
public class HIPMessageServer {

    protected String input1;
    protected String input2;

    /**
     * ��ȡinput1���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getInput1() {
        return input1;
    }

    /**
     * ����input1���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInput1(String value) {
        this.input1 = value;
    }

    /**
     * ��ȡinput2���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getInput2() {
        return input2;
    }

    /**
     * ����input2���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInput2(String value) {
        this.input2 = value;
    }

}
