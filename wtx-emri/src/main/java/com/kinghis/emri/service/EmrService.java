package com.kinghis.emri.service;

import com.kinghis.emri.model.EmriModel;

public interface EmrService {

    /**
     * @DESC: 操作接口信息
     * @Author: liubo
     * @Date: 2020/9/15 3:09 下午
     */
    String operInterface(EmriModel emriModel);

    /**
     * @DESC: WebService
     * @Author: zhoujiao
     * @Date: 2020/9/15 3:09 下午
     */
    String operWebService(EmriModel emriModel);

    /**
     * @DESC: http接口数据
     * @Author: liubo
     * @Date: 2021/1/13 下午3:39
     */
    String operHttp(EmriModel emriModel);


}
