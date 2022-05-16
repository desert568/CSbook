package com.kinghis.yyoauth.util;
/**
 *
 * @Date：2020/3/3
 *
 * @author zhoujiao
 */

import com.kinghis.yyoauth.pojo.T_ftp_info;
import com.wtx.common.exception.CommonException;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Desc java类作用描述
 * @Author zhoujiao
 * @Date 2020/3/3$ 9:56$
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     *  文件上传
     * @param multipartFile
     * @return
     * @throws Exception
     */
    public static Map<String, Object> upladFile(T_ftp_info server, MultipartFile multipartFile, String path, String fileName, boolean isRoot) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        //获取上传文件的输入流
        InputStream inputStream = multipartFile.getInputStream();

        FTPClient ftp = FtpUtil.getFTPClient(server.getServerIp(), server.getFtpPort(), server.getFtpUser(), server.getFtpPwd());
        String ftpPath = server.getFileRootPath() + "/" + path;
        if (isRoot){
            ftpPath = path;
        }


        boolean flag = FtpUtil.uploadSignInputStream(ftp, inputStream, ftpPath, fileName);
        resultMap.put("ftpPath", ftpPath);
        resultMap.put("root", server.getFileRootPath());
        Integer code = 0;
        String msg = "文件上传失败";
        if (flag) {
            code = 1;
            msg = "文件上传成功";
        }

        logger.info(msg);
        resultMap.put("msg", msg);
        resultMap.put("code", code);
        FtpUtil.closeFTP(ftp);

        return resultMap;
    }


    /**
     * 文件下载
     * @param server
     * @param ftpPath
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Map<String, Object> downFile(T_ftp_info server, String ftpPath, String fileName,String watermark) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        FTPClient ftp = FtpUtil.getFTPClient(server.getServerIp(), server.getFtpPort(), server.getFtpUser(), server.getFtpPwd());
        String base64 = FtpUtil.downLoadSignBase64(ftp, server.getFileRootPath() + ftpPath, fileName, watermark);
        FtpUtil.closeFTP(ftp);
        logger.info("文件下载成功");
        resultMap.put("msg", "文件下载成功");
        resultMap.put("code", 1);
        resultMap.put("file", base64);
        return resultMap;
    }

    /**
     * 文件删除
     * @param server
     * @param ftpPath
     * @return
     * @throws Exception
     */
    public Map<String, Object> deleteFile(T_ftp_info server,String ftpPath ) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            FTPClient ftp = FtpUtil.getFTPClient(server.getServerIp(), server.getFtpPort(), server.getFtpUser(), server.getFtpPwd());
            boolean result = FtpUtil.deleteByFolder(ftp, ftpPath);
            FtpUtil.closeFTP(ftp);
            resultMap.put("msg", "文件删除成功");
            resultMap.put("status", true);
            resultMap.put("result", result);
            FtpUtil.closeFTP(ftp);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 文件删除
     * @param server
     * @param ftpPath
     * @return
     * @throws Exception
     */
    public static Map<String, Object> deleteFile(T_ftp_info server,String ftpPath,String fileName) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            FTPClient ftp = FtpUtil.getFTPClient(server.getServerIp(), server.getFtpPort(), server.getFtpUser(), server.getFtpPwd());
            FtpUtil.deleteFile(ftp, server.getFileRootPath() + ftpPath,fileName);
            FtpUtil.closeFTP(ftp);
            resultMap.put("msg", "文件删除成功");
            resultMap.put("code", 1);
            FtpUtil.closeFTP(ftp);
        }catch (Exception e){
            e.printStackTrace();
            throw new CommonException(e.getMessage());
        }
        return resultMap;
    }
}

