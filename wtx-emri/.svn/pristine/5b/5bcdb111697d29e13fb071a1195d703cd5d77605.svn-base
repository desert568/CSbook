package com.kinghis.emri.task;

import com.kinghis.emri.model.EmriModel;
import com.kinghis.emri.pojo.T_emri_sql;
import com.kinghis.emri.service.ErrorService;
import com.kinghis.emri.service.SQLService;
import com.kinghis.emri.service.SourceService;
import com.kinghis.emri.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @DESC: 执行调用接口Callable
 * @Author: liubo
 * @Date: 2020/9/18 9:29 上午
 */
public class InterfaceCallable implements Callable<Map<String, Integer>> {
    private static final Logger log = LoggerFactory.getLogger(InterfaceCallable.class);
    private final List<T_emri_sql> emriSqlList;
    private final CountDownLatch latch;
    private final SourceService sourceService;
    private final SQLService sqlService;
    private final EmriModel emriModel;
    private final ErrorService errorService;


    public InterfaceCallable(List<T_emri_sql> emriSqlList, EmriModel emriModel,
                             SourceService sourceService, SQLService sqlService,
                             ErrorService errorService, CountDownLatch latch) {
        this.emriSqlList = emriSqlList;
        this.latch = latch;
        this.sourceService = sourceService;
        this.emriModel = emriModel;
        this.sqlService = sqlService;
        this.errorService = errorService;
    }

    @Override
    public Map<String, Integer> call() {
        Map<String, Integer> map = new HashMap<>();
        ThreadPoolExecutor executor = ThreadUtil.getThreadPool();
        CountDownLatch latch1 = new CountDownLatch(emriSqlList.size());
        List<Future<Map<String, Integer>>> futureList = new ArrayList<>();
        try {
            for (T_emri_sql info : emriSqlList) {
                Future<Map<String, Integer>> future = executor.submit(new InterfaceInsertCallable(info, sourceService, emriModel, sqlService, errorService, latch1));
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
}
