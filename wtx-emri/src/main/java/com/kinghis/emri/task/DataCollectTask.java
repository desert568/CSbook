package com.kinghis.emri.task;

import com.kinghis.common.util.SpringUtil;
import com.kinghis.emri.config.PropertiesConfig;
import com.kinghis.emri.model.EmriModel;
import com.kinghis.emri.service.EmrService;
import com.wtx.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-18 9:21
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class DataCollectTask {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectTask.class);

    @Value("${day_before}")
    private String day_before;

    //3.添加定时任务 每天晚上2点 质控前一天的数据
    @Scheduled(cron = "${cron}")
    public void collect() {
        logger.info("定时采集电子病历接口数据开始.....");
        long start = System.currentTimeMillis();
        EmrService emrService = SpringUtil.getBean(EmrService.class);

        LocalDate dateTime = LocalDate.now();
        String end_date = dateTime.minusDays(1).toString();
        int before = Integer.valueOf(CommonUtil.isEmpty(day_before) ? "7" : day_before);
        String start_date = dateTime.minusDays(before).toString();

        EmriModel emriModel = new EmriModel();
        emriModel.setStart_date(start_date);
        emriModel.setEnd_date(end_date);
        emriModel.setPatient_id("");
        if ("1".equals(PropertiesConfig.emrFlag)) {
            //http
            emrService.operHttp(emriModel);
        } else if ("2".equals(PropertiesConfig.emrFlag)) {
            emrService.operWebService(emriModel);
        } else {
            //常规
            emrService.operInterface(emriModel);
        }
        long end = System.currentTimeMillis();
        logger.info("定时采集电子病历接口数据结束...时间跨度" + before + "天....耗时：" + ((end - start) / 1000) + "秒");
    }
}
