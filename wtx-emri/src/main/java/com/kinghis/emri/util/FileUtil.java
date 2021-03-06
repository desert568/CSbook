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
            // size ?????????????????? ????????????????????????
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
        //??????????????????null?????????????????????
        if (file == null || !file.exists()) {
            System.out.println("??????????????????,?????????????????????????????????");
            return;
        }
        //?????????????????????????????????????????????
        File[] files = file.listFiles();
        //?????????????????????????????????
        for (File f : files) {
            //???????????????
            String name = file.getName();
            //????????????????????????????????????,????????????????????????
            if (f.isDirectory()) {
                deleteAllFile(f);
            } else {
                f.delete();
            }
        }
    }
}
