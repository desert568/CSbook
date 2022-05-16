package com.kinghis.yyoauth.controller.api;

import com.alibaba.fastjson.JSON;
import com.kinghis.yyoauth.model.FileModel;
import com.kinghis.yyoauth.service.FileApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2021-01-21 16:05
 */
@RestController
@RequestMapping("/api/file")
public class FileApiController{

    @Autowired
    private FileApiService fileApiService;

    private static Logger log = LoggerFactory.getLogger(FileApiController.class);

    /** 
    * @Description: 文件上传
    * @Author: sl
    * @Date: 2021-01-21 16:07
    */
    @RequestMapping(value = "upLoadFile", method = RequestMethod.POST)
    public Map<String, Object> upLoadFile(@RequestBody FileModel model){
        Map<String, Object> map = fileApiService.uploadFile(model);
        log.info(JSON.toJSONString(map));
        return map;
    }

    /**
    * @Description: 判断文件是否上传成功
    * @Author: sl
    * @Date: 2021-01-22 11:06
    */
    @RequestMapping("checkSuccess")
    public Map<String, Object> chekcSuccess(String fileID){
        Map<String, Object> map = fileApiService.checkSuccess(fileID);
        log.info(JSON.toJSONString(map));
        return map;
    }
    
    /** 
    * @Description: 文件下载
    * @Author: sl 
    * @Date: 2021-01-22 11:19
    */
    @RequestMapping("downloadFile")
    public Map<String, Object> downloadFile(@RequestBody FileModel model){
        Map<String, Object> map = fileApiService.downloadFile(model);
        log.info(JSON.toJSONString(map));
        return map;
    }
    
    /** 
    * @Description: 文件删除
    * @Author: sl 
    * @Date: 2021-01-22 14:12
    */
    @RequestMapping("deleteFile")
    public Map<String, Object> deleteFile(String fileID){
        Map<String, Object> map = fileApiService.deleteFile(fileID);
        log.info(JSON.toJSONString(map));
        return map;
    }
    
    /**
    * @Description: 文件从临时服务器移动到正式服务器
    * @Author: sl 
    * @Date: 2021-01-22 14:40
    */
    @RequestMapping("tempToFormal")
    public Map<String, Object> tempToFormal(String fileID){
        Map<String, Object> map = fileApiService.tempToFormal(fileID);
        log.info(JSON.toJSONString(map));
        return map;
    }

    /**
     * @Description: 文件从正式服务器移动到临时服务器
     * @Author: sl
     * @Date: 2021-01-22 14:40
     */
    @RequestMapping("formalToTemp")
    public Map<String, Object> formalToTemp(String fileID){
        Map<String, Object> map = fileApiService.formalToTemp(fileID);
        log.info(JSON.toJSONString(map));
        return map;
    }
}
