package com.kinghis.emri.task;

import com.alibaba.fastjson.JSONObject;
import com.kinghis.emri.config.UserEnum;
import com.kinghis.emri.model.EmriModel;
import com.kinghis.emri.pojo.T_emri_error;
import com.kinghis.emri.pojo.T_emri_source;
import com.kinghis.emri.pojo.T_emri_sql;
import com.kinghis.emri.service.ErrorService;
import com.kinghis.emri.service.SQLService;
import com.kinghis.emri.service.SourceService;
import com.kinghis.emri.util.ConnUtil;
import com.kinghis.emri.util.FileUtil;
import com.wtx.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @DESC: //TODO 执行接入callable
 * @Author: liubo
 * @Date: 2020/9/18 9:28 上午
 */
public class InsertCallableWebService implements Callable<Map<String, Integer>> {
    private static final Logger log = LoggerFactory.getLogger(InsertCallableWebService.class);
    private final T_emri_sql info;
    private final SourceService sourceService;
    private final CountDownLatch latch;
    private final SQLService sqlService;
    private final ErrorService errorService;
    private final EmriModel emriModel;
    private final Map<String, List<Map<String, Object>>> selectMap;

    public InsertCallableWebService(T_emri_sql info, SourceService sourceService,
                                    EmriModel emriModel, SQLService sqlService,
                                    ErrorService errorService, CountDownLatch latch,
                                    Map<String, List<Map<String, Object>>> selectMap) {
        this.info = info;
        this.sourceService = sourceService;
        this.emriModel = emriModel;
        this.latch = latch;
        this.sqlService = sqlService;
        this.errorService = errorService;
        this.selectMap = selectMap;
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
        List<Map<String, Object>> query = new ArrayList<>();
        Connection conn = null;
        PreparedStatement psmt = null;
        try {

            T_emri_source source = sourceService.getById(info.getSource_code());
            if (source != null) {
                try {
                    conn = ConnUtil.getConnection(source);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(info.getTable_name() + e.getMessage());
                    map.put(info.getTable_name() + e.getMessage(), -1);
                    return map;
                }

                if (conn != null) {
                    try {
                        //数据有效性校验
                       /* if (DataValidity(map)) {
                            return map;
                        }*/
                        //获取表
                        query = selectMap.get(info.getTable_name());
                        ConcurrentHashMap<String, String> deleteMap = new ConcurrentHashMap<>();
                        if (query != null && query.size() > 0) {
                            insertData(errCount, flagCount, query, deleteMap);
                        } else {
                            log.info("》》》》表名为：" + info.getTable_name() + "数据为空》》》》》》");
                            query = new ArrayList<>();
                        }
                        map.put(info.getTable_name() + "_list", query.size());
                        map.put(info.getTable_name() + "_error", errCount.get());
                        map.put("flag", flagCount.get());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("查询错误--》》》" + e.getMessage());
                        return map;
                    }
                    return map;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put(info.getTable_name() + e.getMessage(), -1);
        } finally {
            latch.countDown();
            ConnUtil.colsePs(psmt);
            ConnUtil.colseConn(conn);
        }
        return map;
    }

    /**
     * 数据有效性校验
     *
     * @param map
     * @return
     */
    private boolean DataValidity(Map<String, Integer> map) {
        if (CommonUtil.isEmpty(info.getQuery_sql())) {
            map.put("请配置[" + info.getTable_name() + "]查询SQL", -1);
            return true;
        }
        if (CommonUtil.isEmpty(info.getDelete_sql())) {
            map.put("请配置[" + info.getTable_name() + "]删除SQL", -1);
            return true;
        }
        if (CommonUtil.isEmpty(info.getInsert_sql())) {
            map.put("请配置[" + info.getTable_name() + "]插入SQL", -1);
            return true;
        }
        if (CommonUtil.isNotEmpty(emriModel.getUserName())) {
            String hs = UserEnum.getName(emriModel.getUserName());
            if (CommonUtil.isNotEmpty(hs)) {
                String sql = info.getQuery_sql() + " and hosp_code = " + hs;
                info.setQuery_sql(sql);
            }
        }
        return false;
    }

    /**
     * 插入5张表的方法
     *
     * @param errCount
     * @param flagCount
     * @param query
     * @param deleteMap
     */
    private void insertData(AtomicReference<Integer> errCount, AtomicReference<Integer> flagCount, List<Map<String, Object>> query, ConcurrentHashMap<String, String> deleteMap) {
        for (Map<String, Object> map : query) {
            Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
            String PATIENT_ID = "";
            String VISIT_ID = "";
            String dSql = info.getDelete_sql();
            String isql = info.getInsert_sql();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                String key = entry.getKey();
                if ("PATIENTID".equals(entry.getKey()) || "PATIENT_ID".equals(entry.getKey())) {
                    PATIENT_ID = entry.getValue().toString().trim();
                }
                if ("VISIT_ID".equals(entry.getKey()) || "VISITID".equals(entry.getKey())) {
                    VISIT_ID = entry.getValue().toString().trim();
                }
                if (entry.getValue() != null) {
                    if (dSql.indexOf("#") != -1 && CommonUtil.isNotEmpty(dSql)) {
                        dSql = dSql.replace("#" + entry.getKey() + "#", "'" + entry.getValue() + "'");
                    }
                    if (isql.indexOf("#") != -1 && CommonUtil.isNotEmpty(isql)) {
                        isql = isql.replace("#" + entry.getKey() + "#", "'" + entry.getValue().toString().replaceAll("'", "`").replaceAll("#", "") + "'");
                    }
                }
            }
            int cs = sqlService.queryPatInterfaceCount(PATIENT_ID, VISIT_ID);
            if (0 == cs) {
                try {
                    if (!deleteMap.containsKey(PATIENT_ID.trim() + "_" + VISIT_ID)) {
                        log.info("正在删除--》》》" + info.getTable_name());
                        deleteMap.put(PATIENT_ID.trim() + "_" + VISIT_ID, "delete");
                        sqlService.operSql(dSql.replace("where", "WITH(TABLOCKX) where"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("删除错误--》》》" + e.getMessage());
                    errCount.getAndSet(errCount.get() + 1);
                    addError(PATIENT_ID, VISIT_ID, e.getMessage(), "删除" + info.getTable_name(), JSONObject.toJSONString(map));
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
                    addError(PATIENT_ID, VISIT_ID, e.getMessage(), "插入" + info.getTable_name(), JSONObject.toJSONString(map));
                }
            } else {
                //只计算主表的条数，否则会计算重复
                if ("pat_interface".equals(info.getTable_name()) || "pat_enter_dept".equals(info.getTable_name())) {
                    flagCount.getAndSet(flagCount.get() + 1);
                }
            }
        }
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
}
