package com.kinghis.yyoauth.service.impl;

import com.kinghis.yyoauth.dao.FileInfoMapper;
import com.kinghis.yyoauth.dao.FtpMapper;
import com.kinghis.yyoauth.model.FileModel;
import com.kinghis.yyoauth.pojo.T_file_info;
import com.kinghis.yyoauth.pojo.T_ftp_info;
import com.kinghis.yyoauth.service.FileApiService;
import com.kinghis.yyoauth.util.Base64Util;
import com.kinghis.yyoauth.util.FileUtil;
import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2021-01-22 9:31
 */
@Service
public class FileApiServiceImpl implements FileApiService {

    @Autowired
    private FtpMapper ftpMapper;

    @Resource
    private FileInfoMapper fileInfoMapper;

    /**
    * @Description: 文件上传
    * @Author: sl
    * @Date: 2021-01-22 10:52
    */
    @Override
    public Map<String, Object> uploadFile(FileModel model) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtil.isEmpty(model.getFile())){
            map.put("code", 0);
            map.put("msg", "文件流不可为空");
            return map;
        }
        if (CommonUtil.isEmpty(model.getFileName())){
            map.put("code", 0);
            map.put("msg", "文件名不可为空");
            return map;
        }


        //取临时FTP服务器配置
        T_ftp_info ftp = getFTPInfo("0");
        if (null == ftp){
            map.put("code", 0);
            map.put("msg", "FTP文件服务器未配置");
        }
        Map<String, Object> fileMap = null;
        try {
            MultipartFile multipartFile = Base64Util.base64ToMultipart(model.getFile());
            fileMap = FileUtil.upladFile(ftp, multipartFile, model.getFilePath(), model.getFileName(), false);
            map.putAll(fileMap);
            if (Integer.valueOf(map.get("code").toString()) == 1){
                //保存文件操作记录
                saveFileInfo(ftp, model);
            }
        } catch (Exception e){
            e.printStackTrace();
            map.clear();
            map.put("code", 0);
            map.put("msg", e.getMessage());
        }
        return map;
    }


    @Override
    public Map<String, Object> checkSuccess(String fileID) {
        Map<String, Object> map = new HashMap<>();
        T_file_info info = fileInfoMapper.selectByPrimaryKey(fileID);
        if (info == null){
            map.put("code", 0);
            map.put("msg", "文件未上传");
        } else {
            //取文件服务器 查看文件是否已经上传
            try {
                T_ftp_info ftp = ftpMapper.selectByPrimaryKey(info.getServerId());
                map = FileUtil.downFile(ftp, info.getFilesSaveDirectory(),
                        info.getFilesName(), "");
                if (CommonUtil.isNotEmpty((String)map.get("file"))){
                    map.clear();
                    map.put("code", 1);
                    map.put("msg", "文件已上传");
                } else {
                    map.clear();
                    map.put("code", 0);
                    map.put("msg", "文件未上传");
                }
            } catch (Exception e){
                e.printStackTrace();
                map.clear();
                map.put("code", 0);
                map.put("msg", e.getMessage());
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> downloadFile(FileModel model) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = commonCheck(model.getFileID());
        } catch (Exception e){
            map.clear();
            map.put("code", 0);
            map.put("msg", e.getMessage());
            return map;
        }
        //下载文件
        try {
            T_ftp_info ftpInfo = (T_ftp_info) map.get("ftpInfo");
            T_file_info fileInfo = (T_file_info) map.get("fileInfo");
            map.clear();
            map = FileUtil.downFile(ftpInfo, fileInfo.getFilesSaveDirectory(),
                    fileInfo.getFilesName(), model.getWatermark());
        } catch (Exception e){
            e.printStackTrace();
            map.clear();
            map.put("code", 0);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> deleteFile(String fileID) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = commonCheck(fileID);
        } catch (Exception e){
            map.clear();
            map.put("code", 0);
            map.put("msg", e.getMessage());
            e.printStackTrace();
            return map;
        }
        try {
            T_ftp_info ftpInfo = (T_ftp_info) map.get("ftpInfo");
            if (ftpInfo.getType().equals("1")){
                map.clear();
                map.put("code", 1);
                map.put("msg", "文件已移到正式服务器，不允许删除!");
                return map;
            }
            T_file_info fileInfo = (T_file_info) map.get("fileInfo");
            map.clear();
            map = FileUtil.deleteFile(ftpInfo, fileInfo.getFilesSaveDirectory(), fileInfo.getFilesName());
            //deleteFileInfo(fileID);
        } catch (Exception e){
            e.printStackTrace();
            map.clear();
            map.put("code", 0);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> tempToFormal(String fileID) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = commonCheck(fileID);
        } catch (Exception e){
            map.clear();
            map.put("code", 0);
            map.put("msg", e.getMessage());
            return map;
        }
        try {
            T_ftp_info tempFtp = (T_ftp_info) map.get("ftpInfo");
            T_file_info fileInfo = (T_file_info) map.get("fileInfo");
            if (tempFtp.getType().equals("1")){
                map.clear();
                map.put("code", 1);
                map.put("msg", "文件已经移到正式服务器");
                return map;
            }

            //从临时服务器下载文件 再上传到正式服务器
            map = FileUtil.downFile(tempFtp, fileInfo.getFilesSaveDirectory(),
                    fileInfo.getFilesName(), "");

            String fileBase64 = (String) map.get("file");
            //取正式服务器信息
            T_ftp_info formal = getFTPInfo("1");
            if (CommonUtil.isEmpty(fileBase64)){
                //看正式服务器是否存在文件
                map = FileUtil.downFile(formal, fileInfo.getFilesSaveDirectory(), fileInfo.getFilesName(), "");
                if (CommonUtil.isNotEmpty((String)map.get("file"))){
                    map.clear();
                    map.put("code", 1);
                    map.put("msg", "上传成功");
                } else {
                    map.clear();
                    map.put("code", 0);
                    map.put("msg", "临时文件不存在");
                }
                return map;
            }
            if (!fileBase64.contains(",")){
                fileBase64 = "," + fileBase64;
            }
            //文件流转换成文件
            MultipartFile multipartFile = Base64Util.base64ToMultipart(fileBase64);
            map.clear();
            map = FileUtil.upladFile(formal, multipartFile, fileInfo.getFilesSaveDirectory(), fileInfo.getFilesName(), false);

            //如果上传成功，则删除临时服务器文件
            if (Integer.valueOf(map.get("code").toString()) == 1){
                deleteFile(fileID);
                map.put("msg", "文件移到正式服务器成功");

                //修改文件 服务器ID为正式服务器ID
                updateFileInfo(fileInfo.getId(), formal.getId(), formal.getFileRootPath());
            } else {
                map.put("msg", "文件移到正式服务器失败");
            }
        } catch (Exception e){
            e.printStackTrace();
            map.clear();
            map.put("code", 0);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    @Override
    public Map<String, Object> formalToTemp(String fileID) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = commonCheck(fileID);
        } catch (Exception e){
            map.clear();
            map.put("code", 0);
            map.put("msg", e.getMessage());
            return map;
        }
        try {
            //正式服务器信息
            T_ftp_info formal = (T_ftp_info) map.get("ftpInfo");
            T_file_info fileInfo = (T_file_info) map.get("fileInfo");
            if (formal.getType().equals("0")){
                map.clear();
                map.put("code", 1);
                map.put("msg", "文件已经移到临时服务器");
                return map;
            }

            //取临时服务器信息
            T_ftp_info tempFtp = getFTPInfo("0");

            //从正式服务器下载文件 再上传到临时服务器
            map = FileUtil.downFile(formal, fileInfo.getFilesSaveDirectory(),
                    fileInfo.getFilesName(), "");

            String fileBase64 = (String) map.get("file");
            if (CommonUtil.isEmpty(fileBase64)){
                map.clear();
                map.put("code", 0);
                map.put("msg", "文件不存在");
                return map;
            }
            if (!fileBase64.contains(",")){
                fileBase64 = "," + fileBase64;
            }

            //文件流转换成文件
            MultipartFile multipartFile = Base64Util.base64ToMultipart(fileBase64);

            //上传到临时服务器
            map.clear();
            map = FileUtil.upladFile(tempFtp, multipartFile, fileInfo.getFilesSaveDirectory(), fileInfo.getFilesName(), false);

            //如果上传成功
            if (Integer.valueOf(map.get("code").toString()) == 1){
                map.put("msg", "文件移到临时服务器成功");
                //修改文件服务器ID为临时服务器ID
                updateFileInfo(fileInfo.getId(), tempFtp.getId(), tempFtp.getFileRootPath());
            } else {
                map.put("msg", "文件移到临时服务器失败");
            }
        } catch (Exception e){
            e.printStackTrace();
            map.clear();
            map.put("code", 0);
            map.put("msg", e.getMessage());
        }
        return map;
    }


    private Map<String, Object> commonCheck(String fileID){
        Map<String, Object> map = new HashMap<>();
        if (CommonUtil.isEmpty(fileID)){
            throw new CommonException("文件ID不可为空");
        }
        //取文件信息
        T_file_info fileInfo = fileInfoMapper.selectByPrimaryKey(fileID);
        if (fileInfo == null){
            throw new CommonException("文件不存在");
        }

        //取FTP服务器信息
        T_ftp_info ftpInfo = ftpMapper.selectByPrimaryKey(fileInfo.getServerId());
        if (ftpInfo == null){
            throw new CommonException("文件服务器不存在");
        }
        map.put("fileInfo", fileInfo);
        map.put("ftpInfo", ftpInfo);
        return map;
    }


    private T_ftp_info getFTPInfo(String type){
        T_ftp_info info = new T_ftp_info();
        info.setType(type);
        List<T_ftp_info> list = ftpMapper.select(info);
        if (CommonUtil.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    private void saveFileInfo(T_ftp_info ftp, FileModel model){
        //先删除
        fileInfoMapper.deleteByPrimaryKey(model.getFileID());

        T_file_info info = new T_file_info();
        info.setId(model.getFileID());
        info.setFilesName(model.getFileName());
        info.setFilesSaveDirectory("/" + model.getFilePath());
        info.setRootDirectory(ftp.getFileRootPath());
        info.setServerId(ftp.getId());
        info.setUploadDate(new Date());
        fileInfoMapper.insertSelective(info);
    }

    private void updateFileInfo(String fileID, String ftpID, String rootPath){
        T_file_info info = fileInfoMapper.selectByPrimaryKey(fileID);
        info.setServerId(ftpID);
        info.setRootDirectory(rootPath);
        fileInfoMapper.updateByPrimaryKeySelective(info);
    }
}
