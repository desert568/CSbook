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
            throw new CommonException("????????????????????????!");
        }
        if (CommonUtil.isEmpty(emriModel.getEnd_date())) {
            throw new CommonException("????????????????????????!");
        }
        log.info("??????????????????--?????????????????????:" + emriModel.getStart_date() + " ????????????:" + emriModel.getEnd_date() + " ?????????:" + emriModel.getPatient_id());
        ThreadPoolExecutor executor = ThreadUtil.getThreadPool();

        CountDownLatch latch = null;
        List<Future<Map<String, Integer>>> futureList = new ArrayList<>();
        Future<Map<String, Integer>> future = null;
        try {
            System.out.println("???????????????????????????" + executor.getPoolSize() + "??????????????????????????????????????????" +
                    executor.getQueue().size() + "????????????????????????????????????" + executor.getCompletedTaskCount());
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
                System.out.println("fee?????????");
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
                        str += "??????????????????" + entry.getValue() + "</br>";
                    } else if ("pat_interface_error".equals(entry.getKey()) || "pat_enter_dept_error".equals(entry.getKey())) {
                        str += "???????????????" + entry.getValue() + "</br>";
                    } else if ("pat_interface_list".equals(entry.getKey()) || "pat_enter_dept_list".equals(entry.getKey())) {
                        if (0 == entry.getValue()) {
                            return "????????????????????????????????????";
                        }
                        str += "??????????????????" + entry.getValue() + "</br>";
                    } else if (-1 == entry.getValue()) {
                        str += entry.getKey() + "</br>";
                    }
                } else {
                    String nerror = emriModel.getTable_name() + "_error";
                    String nlist = emriModel.getTable_name() + "_list";
                    if (nerror.equals(entry.getKey())) {
                        str += "???????????????" + entry.getValue() + "</br>";
                    } else if (nlist.equals(entry.getKey())) {
                        if (0 == entry.getValue()) {
                            return "????????????????????????????????????";
                        }
                        str += "??????????????????" + entry.getValue() + "</br>";
                    } else if (-1 == entry.getValue()) {
                        str += entry.getKey() + "</br>";
                    }
                }
            }
            System.out.println("???????????????????????????" + executor.getPoolSize() + "??????????????????????????????????????????" +
                    executor.getQueue().size() + "????????????????????????????????????" + executor.getCompletedTaskCount());
            return str;
        } catch (Exception e) {
            latch.countDown();
            e.printStackTrace();
            log.error(e.getMessage());
            return "??????????????????";
        }
    }

    @Override
    public String operWebService(EmriModel emriModel) {
        if (CommonUtil.isEmpty(emriModel.getStart_date())) {
            throw new CommonException("????????????????????????!");
        }
        if (CommonUtil.isEmpty(emriModel.getEnd_date())) {
            throw new CommonException("????????????????????????!");
        }
        String str = "";
        log.info("??????????????????--?????????????????????:" + emriModel.getStart_date() + " ????????????:" + emriModel.getEnd_date() + " ?????????:" + emriModel.getPatient_id());
        ThreadPoolExecutor executor = ThreadUtil.getThreadPool();
        try {
            CountDownLatch latch = null;
            List<Future<Map<String, Integer>>> futureList = new ArrayList<>();
            Future<Map<String, Integer>> future = null;
            InterfaceCallableWebService callable = null;
            T_emri_sql emri_sql = new T_emri_sql();
            List<T_emri_sql> list = sqlService.queryEmrSqlList(emri_sql);
            if (CommonUtil.isEmpty(emriModel.getPatient_id())) {
                System.out.println("???????????????????????????" + executor.getPoolSize() + "??????????????????????????????????????????" +
                        executor.getQueue().size() + "????????????????????????????????????" + executor.getCompletedTaskCount());
                int page = Integer.valueOf(CommonUtil.isEmpty(day_page) ? "7" : day_page);
                Date startTime = DateHelper.toDate(emriModel.getStart_date(), "yyyy-MM-dd");
                Date endTime = DateHelper.toDate(emriModel.getEnd_date(), "yyyy-MM-dd");
                int count = DateUtil.getDayBetween(startTime, endTime);
                latch = new CountDownLatch(count);
                LocalDateTime st = this.DateToLocalDatetime(startTime);
                LocalDateTime et = this.DateToLocalDatetime(endTime);
                log.info("??????" + count + "?????????>>>>>>>>>>>>>>>>>>");
                for (int i = 1; i <= count; i++) {
                    log.info(">>>>>>>>>>>???" + i + "????????????>>>>>>>>>>>>>>>>>>");
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
            //?????????????????????
            ReturnMsg returnMsg = new ReturnMsg(emriModel, futureList);
            str = returnMsg.invoke().getStr();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("???????????????????????????" + executor.getPoolSize() + "??????????????????????????????????????????" +
                executor.getQueue().size() + "????????????????????????????????????" + executor.getCompletedTaskCount());
        return str;
    }

    @Override
    public String operHttp(EmriModel emriModel) {
        if (CommonUtil.isEmpty(emriModel.getStart_date())) {
            throw new CommonException("????????????????????????!");
        }
        if (CommonUtil.isEmpty(emriModel.getEnd_date())) {
            throw new CommonException("????????????????????????!");
        }
        String str = "";
        log.info("??????????????????--?????????????????????:" + emriModel.getStart_date() + " ????????????:" + emriModel.getEnd_date() + " ?????????:" + emriModel.getPatient_id());
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
                    System.out.println("???????????????????????????" + executor.getPoolSize() + "??????????????????????????????????????????" +
                            executor.getQueue().size() + "????????????????????????????????????" + executor.getCompletedTaskCount());
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
            //?????????????????????
            ReturnMsg returnMsg = new ReturnMsg(emriModel, futureList);
            str = returnMsg.invoke().getStr();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }

        System.out.println("???????????????????????????" + executor.getPoolSize() + "??????????????????????????????????????????" +
                executor.getQueue().size() + "????????????????????????????????????" + executor.getCompletedTaskCount());
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
            // ????????????????????????
            log.info("???????????????????????????" + params);
            return xmlToList(WebService.sendWebService("MES0006", params));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * xml????????????list??????
     *
     * @param xmlObject
     * @return
     */
    public List<Map<String, Object>> xmlToList(Object xmlObject) {
        List<Map<String, Object>> list = new ArrayList();
        String xml = (String) xmlObject;
        if (null == xml || "".equals(xml) || !xml.contains("response")) {
            log.info("xml??????");
            return null;
        }
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            Element root = document.getRootElement();//???????????????
            List<Element> childElements = root.elements();//???????????????????????????????????????
            for (Element child : childElements) {//????????????????????????
                Map<String, Object> map = new HashMap();//????????????
                List<Element> infos = child.elements();//??????????????????
                for (Element info : infos) {
                    String name = info.getName().toUpperCase();//?????????????????????
                    String text = info.getText();//?????????????????????
                    map.put(name, text);
                }
                list.add(map);
                map = null;//??????map
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
