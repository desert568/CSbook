package com.kinghis.emri.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kinghis.emri.config.PropertiesConfig;
import com.kinghis.emri.model.EmriModel;
import com.kinghis.emri.pojo.T_emri_error;
import com.kinghis.emri.pojo.T_emri_sql;
import com.kinghis.emri.service.ErrorService;
import com.kinghis.emri.service.SQLService;
import com.kinghis.emri.util.FileUtil;
import com.wtx.common.util.CommonUtil;
import com.wtx.common.util.HttpUtil;
import com.wtx.common.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @DESC: 执行接入callable
 * @Author: liubo
 * @Date: 2020/9/18 9:28 上午
 */
public class InsertHttpCallable implements Callable<Map<String, Integer>> {
    private static final Logger log = LoggerFactory.getLogger(InterfaceInsertCallable.class);
    private final T_emri_sql info;
    private final CountDownLatch latch;
    private final SQLService sqlService;
    private final ErrorService errorService;
    private final EmriModel emriModel;
    private final String httpUrl;

    public InsertHttpCallable(T_emri_sql info,
                              EmriModel emriModel, SQLService sqlService,
                              ErrorService errorService, CountDownLatch latch,String httpUrl) {
        this.info = info;
        this.emriModel = emriModel;
        this.latch = latch;
        this.sqlService = sqlService;
        this.errorService = errorService;
        this.httpUrl = httpUrl;
    }

    private static String replace(String str) {
        if (str.indexOf("#") != -1) {
            int i = str.indexOf("#") + 1;
            String s = str.substring(i);
            int j = s.indexOf("#") + i;
            String b = str.substring(i - 1, j + 1);
            String re = str.replace(b, "null");
            if (re.indexOf("#") != -1) {
                return replace(re);
            } else {
                return re;
            }
        }
        return str;
    }

    @Override
    public Map<String, Integer> call() {
        Map<String, Integer> map = new HashMap<>();
        AtomicReference<Integer> errCount = new AtomicReference<>(0);
        AtomicReference<Integer> flagCount = new AtomicReference<>(0);
        List<Map<String, String>> queryList = null;
        try {
            JSONObject query = null;
            try {
                if (CommonUtil.isEmpty(info.getQuery_sql())) {
                    map.put("请配置[" + info.getTable_name() + "]查询SQL", -1);
                    return map;
                }
                if (CommonUtil.isEmpty(info.getDelete_sql())) {
                    map.put("请配置[" + info.getTable_name() + "]删除SQL", -1);
                    return map;
                }
                if (CommonUtil.isEmpty(info.getInsert_sql())) {
                    map.put("请配置[" + info.getTable_name() + "]插入SQL", -1);
                    return map;
                }
                Map requstMap = new HashMap();
                requstMap.put("ServiceName", "IBaInterFaceService");
                requstMap.put("IsCompress", "false");
                if (CommonUtil.isNotEmpty(PropertiesConfig.orgCode)) {
                    requstMap.put("org_code", PropertiesConfig.orgCode);
                }
                Map parameter = new HashMap();
                parameter.put("Cysj_Begin", emriModel.getStart_date() + " 00:00:00");
                parameter.put("Cysj_End", emriModel.getEnd_date() + " 23:59:59");
                if (CommonUtil.isEmpty(emriModel.getPatient_id())) {
                    parameter.put("Bah", "");
                } else {
                    parameter.put("Bah", emriModel.getPatient_id().trim());
                }
                if("pat_interface_other".equals(info.getTable_name())){
                    parameter.put("ISBASYFJX", PropertiesConfig.isbasyfjx);
                }
                parameter.put("IS_DELETE", "");
                requstMap.put("Parameter", parameter);
                requstMap.put("InterfaceName", info.getQuery_sql());
                query = JSONObject.parseObject(HttpUtil.post(httpUrl, JSONUtil.toJSON(requstMap), -1));
                if (!"true".equals(query.getString("Success"))) {
                    System.out.println("His5.0接口错误");
                }
                JSONObject data = JSONObject.parseObject(query.getString("data"));
                JSONObject value = JSONObject.parseObject(data.getString("Value"));
                if (!"200".equals(value.getString("Code"))) {
                    System.out.println(value.getString("Message"));
                }
                String str = value.getString("Data");
                System.out.println("=================================="+value.getString("Data"));
                if (str != null && "" != str) {
                    queryList = convertResultSetToArrayList(JSONUtil.fromArrayJSON(value.getString("Data"), Map.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("查询错误--》》》" + e.getMessage());
                addError(emriModel.getPatient_id(), "", e.getMessage(), "查询" + info.getTable_name(), JSONArray.toJSONString(query));
                return map;
            }
            ConcurrentHashMap<String, String> deleteMap = new ConcurrentHashMap<>();
            if (queryList != null && queryList.size() > 0) {
                System.out.println("---------数据大小----------："+queryList.size());
                queryList.stream().sequential().forEach(ls -> {

                    String dSql = info.getDelete_sql();
                    String isql = info.getInsert_sql();
                    String PATIENT_ID = "";
                    String VISIT_ID = "";
                    Iterator<Map.Entry<String, String>> it = ls.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> entry = it.next();
                        if ("PATIENT_ID".equals(entry.getKey()) || "PATIENTID".equals(entry.getKey())) {
                            PATIENT_ID = entry.getValue();
                        }
                        if ("VISIT_ID".equals(entry.getKey()) || "VISITID".equals(entry.getKey())) {
                            VISIT_ID = entry.getValue();
                        }
                        if (entry.getValue() != null) {
                            if (dSql.indexOf("#") != -1 && CommonUtil.isNotEmpty(dSql)) {
                                dSql = dSql.replace("#" + entry.getKey() + "#", "'" + entry.getValue() + "'");
                            }
                            if (isql.indexOf("#") != -1 && CommonUtil.isNotEmpty(isql)) {
                                isql = isql.replace("#" + entry.getKey() + "#", "'" + entry.getValue().replaceAll("'", "`").replaceAll("#", "") + "'");
                            }
                        }
                    }
                    int cs = 0;
                    if (!"pat_fee".equals(info.getTable_name()) && !"pat_interface_fee".equals(info.getTable_name())) {
                        cs = sqlService.queryPatInterfaceCount(PATIENT_ID, VISIT_ID);
                    }
                    if (0 == cs) {
                        try {
                            if (!deleteMap.containsKey(PATIENT_ID.trim() + "_" + VISIT_ID)) {
                                log.info("正在删除--》》》" + info.getTable_name());
                                deleteMap.put(PATIENT_ID.trim() + "_" + VISIT_ID, "de lete");
                                sqlService.operSql(dSql.replace("where", "WITH(TABLOCKX) where"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("删除错误--》》》" + e.getMessage());
                            errCount.getAndSet(errCount.get() + 1);
                            addError(PATIENT_ID, VISIT_ID, e.getMessage(), "删除" + info.getTable_name(), JSONObject.toJSONString(ls));
                            return;
                        }
                        try {
                            log.info("正在插入--》》》" + info.getTable_name());
                            sqlService.operSql(replace(isql).replaceAll("'null'", "null"));
                            sqlService.operSql("delete from t_emri_error where oper = '插入" + info.getTable_name() + "' and  patient_id = '" + PATIENT_ID + "' and visit_id = '" + VISIT_ID + "'");
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("插入错误--》》》" + e.getMessage());
                            errCount.getAndSet(errCount.get() + 1);
                            addError(PATIENT_ID, VISIT_ID, e.getMessage(), "插入" + info.getTable_name(), JSONObject.toJSONString(ls));
                        }
                    } else {
                        //只计算主表的条数，否则会计算重复
                        if ("pat_interface".equals(info.getTable_name()) || "pat_enter_dept".equals(info.getTable_name())) {
                            flagCount.getAndSet(flagCount.get() + 1);
                        }
                    }
                });
            } else {
                if ("pat_interface".equals(info.getTable_name()) || "pat_enter_dept".equals(info.getTable_name())) {
                    addError(emriModel.getPatient_id(), "", "在电子病历提供视图中未找到数据", "查询" + info.getTable_name(), "");
                }
            }
            map.put(info.getTable_name() + "_list", query.size());
            map.put(info.getTable_name() + "_error", errCount.get());
            map.put("flag", flagCount.get());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put(info.getTable_name() + e.getMessage(), -1);
        } finally {
            latch.countDown();
        }
        return map;
    }

    private void addError(String pid, String vid, String msg, String table, String source) {
        if (pid.indexOf("%") != -1) {
            pid = pid.replace("%", "");
        }
        String delErr = "delete from t_emri_error where oper = '" + table + "' and patient_id = '" + pid + "' and start_date>= '" + emriModel.getStart_date() + "' and end_date <= '" + emriModel.getEnd_date() + "'";
        if (CommonUtil.isNotEmpty(vid)) {
            delErr += " and visit_id = '" + vid + "'";
        }
        sqlService.operSql(delErr);
        T_emri_error error = new T_emri_error();
        error.setPatient_id(pid);
        error.setVisit_id(vid);
        error.setStart_date(emriModel.getStart_date());
        error.setEnd_date(emriModel.getEnd_date());
        error.setOper(table);
        error.setSource(source.replaceAll("'", "`"));
        error.setError_msg(msg);
        errorService.add(error);
        log.info("日志记录成功--》》》");
    }

    private List<Map<String, String>> convertResultSetToArrayList(List<Map> ls) throws Exception {
        List<Map<String, String>> tempList = new ArrayList();
        for (int i = 0; i < ls.size(); i++) {
            Map<String, String> map = ls.get(i);
            Map<String, String> rmap = new HashMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String mapKey = entry.getKey().toUpperCase();
                String mapValue = entry.getValue();
                rmap.put(mapKey, mapValue);
            }
            tempList.add(rmap);
        }
        return tempList;
    }
}
