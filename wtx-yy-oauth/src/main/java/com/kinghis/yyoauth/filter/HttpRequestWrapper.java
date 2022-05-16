package com.kinghis.yyoauth.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HttpRequestWrapper extends HttpServletRequestWrapper {

    public HttpRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter){
        String[] values = super.getParameterValues(parameter);
        if (values == null){
            return null;
        }
        int count = values.length;
        // 遍历每一个参数，检查是否含有
        String[] encodedValues = new String[count];
        for(int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if(value == null) {
            return null;
        }
        return cleanXSS(value);
    }



    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null){
            return null;
        }
        return cleanXSS(value);
    }

    private String cleanXSS(String value){
        return this.htmlEncode(value).trim();
        //return value.trim();
    }

    private String htmlEncode(String source) {
        if (source == null) {
            return "";
        }
        String html = "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (c) {
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                //case '$':
                //    break;
                default:
                    buffer.append(c);
                    break;
            }
        }
        html = buffer.toString();
        return html;
    }
}
