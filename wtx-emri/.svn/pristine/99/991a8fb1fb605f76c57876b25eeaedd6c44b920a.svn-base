package com.kinghis.emri.util;

import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2021-11-26 9:57
 */
public class ParamsUtil {

    public static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|" +
            "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|"+
            "ascii|declare|exec|count|master|drop|execute)\\b)";

    public static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE); //忽略大小写

    public static String isSqlValid(String str){
        if (CommonUtil.isNotEmpty(str)){
            Matcher matcher = sqlPattern.matcher(str);
            if (matcher.find()){
                throw new CommonException("存在非法参数：" +matcher.group() );
            }
        }
        return "";
    }
}
