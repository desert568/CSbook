package com.kinghis.emri.service.impl;

import cn.trasen.commons.util.DateUtil;
import com.kinghis.emri.config.PropertiesConfig;
import com.kinghis.emri.model.EmriModel;
import com.kinghis.emri.model.ReturnMsg;
import com.kinghis.emri.pojo.T_emri_sql;
import com.kinghis.emri.service.EmrService;
import com.kinghis.emri.service.ErrorService;
import com.kinghis.emri.service.SQLService;
import com.kinghis.emri.service.SourceService;
import com.kinghis.emri.task.InterfaceCallable;
import com.kinghis.emri.task.InterfaceCallableWebService;
import com.kinghis.emri.task.InterfaceHttpCallable;
import com.kinghis.emri.util.FileUtil;
import com.kinghis.emri.util.ThreadUtil;
import com.kinghis.emri.webservice.WebService;
import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import com.wtx.common.util.DateHelper;
import com.wtx.common.util.ObjectUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class EmrServiceImpl implements EmrService {

    private static final Logger log = LoggerFactory.getLogger(EmrServiceImpl.class);

    @Autowired
    private SQLService sqlService;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private ErrorService errorService;

    @Value("${day_page}")
    private String day_page;

    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>();
        list1.add("a");
        List<String> list2 = new ArrayList<>();
        list2.addAll(list1);

        list1.add("b");

        System.out.println(list1.size());
        System.out.println(list2.size());
    }

    @Override
    public String operInterface(EmriModel emriModel) {
        if (CommonUtil.isEmpty(emriModel.getStart_date())) {
            throw new CommonException("开始时间不能为空!");
        }
        if (CommonUtil.isEmpty(emriModel.getEnd_date())) {
            throw new CommonException("结束时间不能为空!");
        }
        log.info("程序接口入参--》》》开始时间:" + emriModel.getStart_date() + " 结束时间:" + emriModel.getEnd_date() + " 病案号:" + emriModel.getPatient_id());
        ThreadPoolExecutor executor = ThreadUtil.getThreadPool();

        CountDownLatch latch = null;
        List<Future<Map<String, Integer>>> futureList = new ArrayList<>();
        Future<Map<String, Integer>> future = null;
        try {
            System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                    executor.getQueue().size() + "，已执行结束的任务数目：" + executor.getCompletedTaskCount());
            InterfaceCallable callable = null;
            T_emri_sql emri_sql = new T_emri_sql();
            if (CommonUtil.isNotEmpty(emriModel.getTable_name())) {
                emri_sql.setTable_name(emriModel.getTable_name());
            }
            List<T_emri_sql> list = sqlService.queryEmrSqlList(emri_sql);

            List<T_emri_sql> list1 = new ArrayList<>();
            List<T_emri_sql> list2 = new ArrayList<>();
            for (T_emri_sql info : list) {
                if ("pat_interface_fee".equals(info.getTable_name()) || "pat_fee".equals(info.getTable_name())) {
                    list1.add(info);
                } else {
                    list2.add(info);
                }
            }
            if (list2 != null && list2.size() > 0) {
                latch = new CountDownLatch(1);
                callable = new InterfaceCallable(list2, emriModel, sourceService, sqlService, errorService, latch);
                future = executor.submit(callable);
                futureList.add(future);
                latch.await();
            }

            if (list1 != null && list1.size() > 0) {
                System.out.println("fee表执行");
                latch = new CountDownLatch(1);
                callable = new InterfaceCallable(list1, emriModel, sourceService, sqlService, errorService, latch);
                future = executor.submit(callable);
                futureList.add(future);
                latch.await();
            }
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
            String str = "";
            while (iter.hasNext()) {
                Map.Entry<String, Integer> entry = iter.next();
                if (CommonUtil.isEmpty(emriModel.getTable_name())) {
                    if ("flag".equals(entry.getKey())) {
                        str += "已录入条数：" + entry.getValue() + "</br>";
                    } else if ("pat_interface_error".equals(entry.getKey()) || "pat_enter_dept_error".equals(entry.getKey())) {
                        str += "错误条数：" + entry.getValue() + "</br>";
                    } else if ("pat_interface_list".equals(entry.getKey()) || "pat_enter_dept_list".equals(entry.getKey())) {
                        if (0 == entry.getValue()) {
                            return "在源头数据库中未找到数据";
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
                            return "在源头数据库中未找到数据";
                        }
                        str += "总数据条数：" + entry.getValue() + "</br>";
                    } else if (-1 == entry.getValue()) {
                        str += entry.getKey() + "</br>";
                    }
                }
            }
            System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                    executor.getQueue().size() + "，已执行结束的任务数目：" + executor.getCompletedTaskCount());
            return str;
        } catch (Exception e) {
            latch.countDown();
            e.printStackTrace();
            log.error(e.getMessage());
            return "接口信息错误";
        }
    }

    @Override
    public String operWebService(EmriModel emriModel) {
        if (CommonUtil.isEmpty(emriModel.getStart_date())) {
            throw new CommonException("开始时间不能为空!");
        }
        if (CommonUtil.isEmpty(emriModel.getEnd_date())) {
            throw new CommonException("结束时间不能为空!");
        }
        String str = "";
        log.info("程序接口入参--》》》开始时间:" + emriModel.getStart_date() + " 结束时间:" + emriModel.getEnd_date() + " 病案号:" + emriModel.getPatient_id());
        ThreadPoolExecutor executor = ThreadUtil.getThreadPool();
        try {
            CountDownLatch latch = null;
            List<Future<Map<String, Integer>>> futureList = new ArrayList<>();
            Future<Map<String, Integer>> future = null;
            InterfaceCallableWebService callable = null;
            T_emri_sql emri_sql = new T_emri_sql();
            List<T_emri_sql> list = sqlService.queryEmrSqlList(emri_sql);
            if (CommonUtil.isEmpty(emriModel.getPatient_id())) {
                System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                        executor.getQueue().size() + "，已执行结束的任务数目：" + executor.getCompletedTaskCount());
                int page = Integer.valueOf(CommonUtil.isEmpty(day_page) ? "7" : day_page);
                Date startTime = DateHelper.toDate(emriModel.getStart_date(), "yyyy-MM-dd");
                Date endTime = DateHelper.toDate(emriModel.getEnd_date(), "yyyy-MM-dd");
                int count = DateUtil.getDayBetween(startTime, endTime);
                latch = new CountDownLatch(count);
                LocalDateTime st = this.DateToLocalDatetime(startTime);
                LocalDateTime et = this.DateToLocalDatetime(endTime);
                log.info("一共" + count + "个线程>>>>>>>>>>>>>>>>>>");
                for (int i = 1; i <= count; i++) {
                    log.info(">>>>>>>>>>>第" + i + "线程开始>>>>>>>>>>>>>>>>>>");
                    if (i != 1 && st.isBefore(et)) {
                        st = st.plusDays(1);
                    }
                    String s_start = st.toLocalDate().toString();
                    emriModel.setStart_date(s_start);
                    //Map<String, List<Map<String, Object>>> map = sendService(emriModel);
                    EmriModel newModel = new EmriModel();
                    ObjectUtil.copyInfo(emriModel, newModel, null);
                    if (st.plusDays(page).compareTo(et) >= 0) {
                        callable = new InterfaceCallableWebService(list, newModel, sourceService, sqlService, errorService, latch);
                    } else {
                        st = st.plusDays(page);
                        String s_end = st.toLocalDate().toString();
                        newModel.setEnd_date(s_end);
                        callable = new InterfaceCallableWebService(list, newModel, sourceService, sqlService, errorService, latch);
                    }
                    future = executor.submit(callable);
                    futureList.add(future);
                }
            } else {
                latch = new CountDownLatch(1);
                callable = new InterfaceCallableWebService(list, emriModel, sourceService, sqlService, errorService, latch);
                future = executor.submit(callable);
                futureList.add(future);

            }
            latch.await();
            //获取返回字符串
            ReturnMsg returnMsg = new ReturnMsg(emriModel, futureList);
            str = returnMsg.invoke().getStr();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                executor.getQueue().size() + "，已执行结束的任务数目：" + executor.getCompletedTaskCount());
        return str;
    }

    @Override
    public String operHttp(EmriModel emriModel) {
        if (CommonUtil.isEmpty(emriModel.getStart_date())) {
            throw new CommonException("开始时间不能为空!");
        }
        if (CommonUtil.isEmpty(emriModel.getEnd_date())) {
            throw new CommonException("结束时间不能为空!");
        }
        String str = "";
        log.info("程序接口入参--》》》开始时间:" + emriModel.getStart_date() + " 结束时间:" + emriModel.getEnd_date() + " 病案号:" + emriModel.getPatient_id());
        ThreadPoolExecutor executor = ThreadUtil.getThreadPool();
        CountDownLatch latch = null;
        try {
            List<Future<Map<String, Integer>>> futureList = new ArrayList<>();
            Future<Map<String, Integer>> future = null;
            InterfaceHttpCallable callable = null;
            T_emri_sql emri_sql = new T_emri_sql();
            if (CommonUtil.isNotEmpty(emriModel.getTable_name())) {
                emri_sql.setTable_name(emriModel.getTable_name());
            }
            List<T_emri_sql> list = sqlService.queryEmrSqlList(emri_sql);
            String httpUrl = PropertiesConfig.httpUrl;

            List<T_emri_sql> list1 = new ArrayList<>();
            List<T_emri_sql> list2 = new ArrayList<>();
            for (T_emri_sql info : list) {
                if ("pat_interface_fee".equals(info.getTable_name()) || "pat_fee".equals(info.getTable_name())) {
                    list1.add(info);
                } else {
                    list2.add(info);
                }
            }
            if (list2 != null && list2.size() > 0) {
                if (CommonUtil.isEmpty(emriModel.getPatient_id())) {
                    System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                            executor.getQueue().size() + "，已执行结束的任务数目：" + executor.getCompletedTaskCount());
                    int page = Integer.valueOf(CommonUtil.isEmpty(day_page) ? "7" : day_page);
                    Date startTime = DateHelper.toDate(emriModel.getStart_date(), "yyyy-MM-dd");
                    Date endTime = DateHelper.toDate(emriModel.getEnd_date(), "yyyy-MM-dd");
                    int count = ThreadUtil.getThreadCountByDate(startTime, endTime, page);
                    latch = new CountDownLatch(count);
                    LocalDateTime st = this.DateToLocalDatetime(startTime);
                    LocalDateTime et = this.DateToLocalDatetime(endTime);
                    for (int i = 1; i <= count; i++) {
                        if (i != 1 && st.isBefore(et)) {
                            st = st.plusDays(1);
                        }
                        String s_start = st.toLocalDate().toString();
                        emriModel.setStart_date(s_start);
                        EmriModel newModel = new EmriModel();
                        ObjectUtil.copyInfo(emriModel, newModel, null);
                        if (st.plusDays(page).compareTo(et) >= 0) {
                            callable = new InterfaceHttpCallable(list2, newModel, sqlService, errorService, latch, httpUrl);
                        } else {
                            st = st.plusDays(page);
                            String s_end = st.toLocalDate().toString();
                            newModel.setEnd_date(s_end);
                            callable = new InterfaceHttpCallable(list2, newModel, sqlService, errorService, latch, httpUrl);
                        }
                        future = executor.submit(callable);
                        futureList.add(future);
                    }
                } else {
                    latch = new CountDownLatch(1);
                    callable = new InterfaceHttpCallable(list, emriModel, sqlService, errorService, latch, httpUrl);
                    future = executor.submit(callable);
                    futureList.add(future);
                }
                latch.await();
            }
            if (list1 != null && list1.size() > 0) {
                latch = new CountDownLatch(1);
                callable = new InterfaceHttpCallable(list1, emriModel, sqlService, errorService, latch, httpUrl);
                future = executor.submit(callable);
                futureList.add(future);
                latch.await();
            }
            //获取返回字符串
            ReturnMsg returnMsg = new ReturnMsg(emriModel, futureList);
            str = returnMsg.invoke().getStr();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }

        System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                executor.getQueue().size() + "，已执行结束的任务数目：" + executor.getCompletedTaskCount());
        return str;
    }

    private LocalDateTime DateToLocalDatetime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return localDateTime;
    }

    private List<Map<String, Object>> sendMES0006(String params) {
        try {
            // 获取出院病历诊断
            log.info("获取就诊号，日期：" + params);
            return xmlToList(WebService.sendWebService("MES0006", params));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * xml字符串转list集合
     *
     * @param xmlObject
     * @return
     */
    public List<Map<String, Object>> xmlToList(Object xmlObject) {
        List<Map<String, Object>> list = new ArrayList();
        String xml = (String) xmlObject;
        if (null == xml || "".equals(xml) || !xml.contains("response")) {
            log.info("xml为空");
            return null;
        }
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            Element root = document.getRootElement();//获取根元素
            List<Element> childElements = root.elements();//获取当前元素下的全部子元素
            for (Element child : childElements) {//循环输出全部信息
                Map<String, Object> map = new HashMap();//每个实体
                List<Element> infos = child.elements();//每个对象信息
                for (Element info : infos) {
                    String name = info.getName().toUpperCase();//获取当前元素名
                    String text = info.getText();//获取当前元素值
                    map.put(name, text);
                }
                list.add(map);
                map = null;//回收map
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
