package com.kinghis.emri.task;

import cn.trasen.commons.util.DateUtil;
import cn.trasen.commons.util.StringUtil;
import com.kinghis.emri.model.EmriModel;
import com.kinghis.emri.pojo.T_emri_sql;
import com.kinghis.emri.service.ErrorService;
import com.kinghis.emri.service.SQLService;
import com.kinghis.emri.service.SourceService;
import com.kinghis.emri.util.DataConversion;
import com.kinghis.emri.util.FileUtil;
import com.kinghis.emri.util.ThreadUtil;
import com.kinghis.emri.webservice.WebService;
import com.wtx.common.util.DateHelper;
import com.wtx.common.util.ObjectUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @DESC: 执行调用接口Callable
 * @Author: liubo
 * @Date: 2020/9/18 9:29 上午
 */
public class InterfaceCallableWebService implements Callable<Map<String, Integer>> {
    private static final Logger log = LoggerFactory.getLogger(InterfaceCallableWebService.class);
    private final List<T_emri_sql> emriSqlList;
    private final CountDownLatch latch;
    private final SourceService sourceService;
    private final SQLService sqlService;
    private final EmriModel emriModel;
    private final ErrorService errorService;


    public InterfaceCallableWebService(List<T_emri_sql> emriSqlList, EmriModel emriModel,
                                       SourceService sourceService, SQLService sqlService,
                                       ErrorService errorService, CountDownLatch latch) {
        this.emriSqlList = emriSqlList;
        this.latch = latch;
        this.sourceService = sourceService;
        this.emriModel = emriModel;
        this.sqlService = sqlService;
        this.errorService = errorService;

    }

    public static void main(String[] args) {

    }

    @Override
    public Map<String, Integer> call() {
        Map<String, Integer> map = new HashMap<>();
        ThreadPoolExecutor executor = ThreadUtil.getThreadPool();
        CountDownLatch latch1 = new CountDownLatch(emriSqlList.size());
        List<Future<Map<String, Integer>>> futureList = new ArrayList<>();
        try {
            Map<String, List<Map<String, Object>>> selectMap = sendService(emriModel);

            for (T_emri_sql info : emriSqlList) {
                Future<Map<String, Integer>> future = executor.submit(new InsertCallableWebService(info, sourceService, emriModel, sqlService, errorService, latch1, selectMap));
                futureList.add(future);
            }
            latch1.await();
            for (Future f : futureList) {
                Map<String, Integer> m = (Map<String, Integer>) f.get();
                for (Map.Entry<String, Integer> e : m.entrySet()) {
                    if (map.containsKey(e.getKey())) {
                        int i = map.get(e.getKey()).intValue();
                        map.put(e.getKey(), i + e.getValue().intValue());
                    } else {
                        map.put(e.getKey(), e.getValue());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            latch.countDown();
        }
        return map;
    }

    private Map<String, List<Map<String, Object>>> sendService(EmriModel emriModel) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        //比较两个时间差
        //获取出院病历基本信息
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> list1 = new ArrayList<>();
        List<Map<String, Object>> list2 = new ArrayList<>();
        List<Map<String, Object>> list3 = new ArrayList<>();
        Date startTime = DateHelper.toDate(emriModel.getStart_date(), "yyyy-MM-dd");
        Date endTime = DateHelper.toDate(emriModel.getEnd_date(), "yyyy-MM-dd");
        int days = DateUtil.getDayBetween(startTime, endTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(startTime);
       /*  String count = WebService.sendWebService("MES0002", date);
       log.info("获取出院病历基本信息，总条数：" + count);*/
        String params = date + "^1^" + 50000 + "^";
        if (!StringUtil.isEmpty(emriModel.getPatient_id())) {
            params = params + emriModel.getPatient_id();
        }

        sendDh(list, list1, list2, list3, date, params);
        result.put("pat_interface_diag", DataConversion.convertDiagListToArrayList(list1));
        log.info("获取出院病历诊断，pat_interface_diag数据条数：" + (DataConversion.convertDiagListToArrayList(list1) == null ? 0
                : DataConversion.convertDiagListToArrayList(list1).size()));
        try {
            result.put("pat_interface_operate", DataConversion.convertOperateListToArrayList(list2));
            log.info("获取pat_interface_operate数据条数：" + (DataConversion.convertOperateListToArrayList(list2) == null ? 0
                    : DataConversion.convertOperateListToArrayList(list2).size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("pat_interface", list);
        log.info("获取pat_interface数据条数：" + (list == null ? 0 : list.size()));

        result.put("pat_interface_other", list3);
        log.info("获取pat_interface_other数据条数：" + (list3 == null ? 0 : list3.size()));
        return result;
    }

    private void sendDh(List<Map<String, Object>> list, List<Map<String, Object>> list1, List<Map<String, Object>> list2, List<Map<String, Object>> list3, String date, String params) {
        try {
            // 获取出院病历诊断
            log.info("获取出院病历诊断，日期：" + date);
            list1.addAll(xmlToList(WebService.sendWebService("MES0003", params)));
            //获取出院病历手术
            log.info("获取出院病历手术，日期：" + date);
            String MES0004 = WebService.sendWebService("MES0004", params);
            log.info("获取出院病历手术，MES0004：" + MES0004);
            list2.addAll(xmlToList(MES0004));
            // 获取出院病历其他信息
            log.info("获取出院病历其他信息，日期：" + date);
            list3.addAll(xmlToList(WebService.sendWebService("MES0005", params)));
            log.info("获取出院病历基本信息，日期：" + date);
            list.addAll(xmlToList(WebService.sendWebService("MES0002", params)));

/*

            log.info("获取出院病历诊断，日期：" + date);
            list1.addAll(xmlToList(FileUtil.getContent("c:", "Users\\Sunny\\Desktop\\webservice\\icd_nam.txt")));
            log.info("获取出院病历手术，日期：" + date);
            list2.addAll(xmlToList(FileUtil.getContent("c:", "Users\\Sunny\\Desktop\\webservice\\ss.txt")));
            log.info("获取出院病历其他信息，日期：" + date);
            list3.addAll(xmlToList(FileUtil.getContent("c:", "Users\\Sunny\\Desktop\\webservice\\other.txt")));
            log.info("获取出院病历基本信息，日期：" + date);
            list.addAll(xmlToList(FileUtil.getContent("c:", "Users\\Sunny\\Desktop\\webservice\\jibenxinx.txt")));
*/


        } catch (Exception e) {
            e.printStackTrace();
        }
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
