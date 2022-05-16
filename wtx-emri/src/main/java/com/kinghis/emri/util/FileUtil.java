package com.kinghis.emri.util;

import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @DESC:
 * @Author: sl
 * @Date: 2020-09-28 13:36
 */
public class FileUtil {

    public static String saveFileForString(String nfsPath, String childPath, String fileName,
                                           String content) {


        String path = nfsPath + "/" + childPath;
        String ext = ".txt";

        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }


        String saveFileName = ((CommonUtil.isEmpty(childPath)) ? "" : childPath) + "/" + fileName + ext;

        /*String savefileAllName = path + "/" + fileName + ext;
        FileWriterWithEncoding fWriter = null;
        try {
            fWriter = new FileWriterWithEncoding(savefileAllName, "UTF-8");
            fWriter.write(content);
        } catch (Exception ex) {
            throw new CommonException(ex);
        } finally {
            try {
                fWriter.flush();
                fWriter.close();
            } catch (Exception ex) {
                throw new CommonException(ex);
            }
        }*/
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriterWithEncoding(path + "/" + fileName + ext,StandardCharsets.UTF_8));
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch (Exception ex) {
            throw new CommonException(ex);
        } finally {
            try {
                if (bufferedWriter != null)
                {
                    bufferedWriter.close();
                }
            } catch (Exception ex) {
                throw new CommonException(ex);
            }
        }
        return saveFileName;
    }

    public static String getContent(String nfsPath, String contenturl) {
        if (CommonUtil.isEmpty(contenturl))
            return "";
        File file = new File(nfsPath + "/" + contenturl);
        String contentStr = "";
        try {
            FileInputStream in = new FileInputStream(file);
            // size 为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            contentStr = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {

        }
        return contentStr;
    }

    public static void deleteAllFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //打印文件名
            String name = file.getName();
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                deleteAllFile(f);
            } else {
                f.delete();
            }
        }
    }
}
