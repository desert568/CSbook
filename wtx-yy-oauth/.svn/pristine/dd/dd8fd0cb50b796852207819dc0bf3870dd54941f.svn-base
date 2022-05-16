package com.kinghis.yyoauth.util;/**
 *
 * @Date：2020/3/10
 *
 * @author zhoujiao
 */

import com.wtx.common.exception.CommonException;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Base64;

/**
 * @Desc java类作用描述
 * @Author zhoujiao
 * @Date 2020/3/10$ 18:45$
 */

public class Base64Util implements MultipartFile {

    private final byte[] imgContent;

    private final String header;

    public Base64Util(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
    }
    @Override
    public String getName() {
        // TODO - implementation depends on your requirements
        return System.currentTimeMillis() + Math.random() + "." + header.split("/")[1];
    }

    @Override
    public String getOriginalFilename() {
        // TODO - implementation depends on your requirements
        return System.currentTimeMillis() + (int) Math.random() * 10000 + "." + header.split("/")[1];
    }

    @Override
    public String getContentType() {
        // TODO - implementation depends on your requirements
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }


    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[1]);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new Base64Util(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException("文件流转换成文件出差");
        }
    }

    /**
      * 将文件转成base64 字符串
      *
      * @param path文件路径
      * @return *
      * @throws Exception
      */


    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }


    /**
      * 将base64字符解码保存文件
      *
      * @param base64Code
      * @param targetPath
      * @throws Exception
      */
    public static void decoderBase64File(String base64Code, String targetPath, String catalogue)
            throws Exception {
        File file = new File(catalogue);
        if (file.exists() == false) {
            file.mkdirs();
        }
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }
    public static void main(String[] args) {
        try {System.out.println("2222");
            String ret = Base64Util.encodeBase64File("F:\\私服\\123.png");
            System.out.println(ret);
        } catch (Exception e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static String base64(String str){
        byte[] byteStr = str.getBytes();
        return Base64.getEncoder().encodeToString(byteStr);
    }

    public static String decodeBase64(String str){
        byte[] byteStr = Base64.getDecoder().decode(str);
        return new String(byteStr );
    }
}

