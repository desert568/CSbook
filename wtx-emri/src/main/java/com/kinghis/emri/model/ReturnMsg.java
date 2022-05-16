package com.kinghis.emri.model;

import com.wtx.common.util.CommonUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @Classname ReturnMsg
 * @Description 返回错误信息
 * @Date 2020-12-27 10:29
 * @Created by Sunny
 */
public class ReturnMsg {

    private boolean myResult;
    private final EmriModel emriModel;
    private final List<Future<Map<String, Integer>>> futureList;
    private String str;

    public ReturnMsg(EmriModel emriModel, List<Future<Map<String, Integer>>> futureList) {
        this.emriModel = emriModel;
        this.futureList = futureList;
    }

    boolean is() {
        return myResult;
    }

    public String getStr() {
        return str;
    }

    public ReturnMsg invoke() throws InterruptedException, java.util.concurrent.ExecutionException {
        Map<String, Integer> map = new HashMap();
        for (Future f : futureList) {
            Map<String, Integer> m = (Map) f.get();
            Iterator<Map.Entry<String, Integer>> iter = m.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Integer> entry = iter.next();
                if (map.containsKey(entry.getKey())) {
                    map.put(entry.getKey(), map.get(entry.getKey()) + entry.getValue());
                } else {
                    map.put(entry.getKey(), entry.getValue());
                }
            }
        }
        Iterator<Map.Entry<String, Integer>> iter = map.entrySet().iterator();
        str = "";
        while (iter.hasNext()) {
            Map.Entry<String, Integer> entry = iter.next();
            if (CommonUtil.isEmpty(emriModel.getTable_name())) {
                if ("flag".equals(entry.getKey())) {
                    str += "已录入条数：" + entry.getValue() + "</br>";
                } else if ("pat_interface_error".equals(entry.getKey()) || "pat_enter_dept_error".equals(entry.getKey())) {
                    str += "错误条数：" + entry.getValue() + "</br>";
                } else if ("pat_interface_list".equals(entry.getKey()) || "pat_enter_dept_list".equals(entry.getKey())) {
                    if (0 == entry.getValue()) {
                        myResult = true;
                        return this;
                    }
                    str += "总数据条数：" + entry.getValue() + "</br>";
                } else if (-1 == entry.getValue()) {
                    str += entry.getKey() + "</br>";
                }
            } else {
                String nerror = emriModel.getTable_name() + "_error";
                String nlist = emriModel.getTable_name() + "_list";
                if (nerror.equals(entry.getKey())) {
                    str += "错误条数：" + entry.getValue() + "</br>";
                } else if (nlist.equals(entry.getKey())) {
                    if (0 == entry.getValue()) {
                        myResult = true;
                        return this;
                    }
                    str += "总数据条数：" + entry.getValue() + "</br>";
                } else if (-1 == entry.getValue()) {
                    str += entry.getKey() + "</br>";
                }
            }
        }
        myResult = false;
        return this;
    }

}
