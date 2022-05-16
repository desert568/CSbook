package com.kinghis.yyoauth.util;

import com.kinghis.yyoauth.common.config.OauthWebConfig;
import com.wtx.common.exception.CommonException;
import com.wtx.common.util.CommonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketException;
import java.util.Base64;

/**
 * @Desc FtpUtil FTP工具类
 * @Author liubo
 * @Date 2020/3/20 10:39
 */
public class FtpUtil {

    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);


    /**
     * 获取FTPClient对象
     *
     * @param ftpHost     服务器IP
     * @param ftpPort     服务器端口号
     * @param ftpUserName 用户名
     * @param ftpPassword 密码
     * @return FTPClient
     */
    public static FTPClient getFTPClient(String ftpHost, int ftpPort, String ftpUserName, String ftpPassword) {

        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            // 连接FPT服务器,设置IP及端口
            ftp.connect(ftpHost, ftpPort);
            // 设置用户名和密码
            ftp.login(ftpUserName, ftpPassword);
            // 设置连接超时时间,5000毫秒
            ftp.setConnectTimeout(50000);
            // 设置中文编码集，防止中文乱码
            ftp.setControlEncoding("UTF-8");
            ftp.enterLocalPassiveMode();        //这个设置允许被动连接--访问远程ftp时需要
            FTPClientConfig config = new FTPClientConfig();
            config.setLenientFutureDates(true);
            ftp.configure(config);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                logger.info("未连接到FTP，用户名或密码错误");
                ftp.disconnect();
            } else {
                System.out.println("FTP连接成功");
                logger.info("FTP连接成功");
            }

        } catch (SocketException e) {
            e.printStackTrace();
            logger.info("FTP的IP地址可能错误，请正确配置");
            throw new CommonException(e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FTP的端口错误,请正确配置");
            throw new CommonException(e);
        }
        return ftp;
    }

    /**
     * 关闭FTP方法
     *
     * @param ftp
     * @return
     */
    public static boolean closeFTP(FTPClient ftp) {

        try {
            ftp.logout();
        } catch (Exception e) {
            logger.error("FTP关闭失败");
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error("FTP关闭失败");
                }
            }
        }

        return false;

    }


    /**
     * 下载FTP下指定文件
     *
     * @param ftp      FTPClient对象
     * @param filePath FTP文件路径
     * @param fileName 文件名
     * @param downPath 下载保存的目录
     * @return
     */
    public static boolean downLoadFTP(FTPClient ftp, String filePath, String fileName, String downPath) {
        // 默认失败
        boolean flag = false;

        try {
            // 跳转到文件目录
            ftp.changeWorkingDirectory(new String(filePath.getBytes("UTF-8"), "ISO-8859-1"));
            // 获取目录下文件集合
            ftp.enterLocalPassiveMode();
            FTPFile[] files = ftp.listFiles();
            for (FTPFile file : files) {
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {
                    File downFile = new File(downPath + File.separator
                            + file.getName());
                    OutputStream out = new FileOutputStream(downFile);
                    // 绑定输出流下载文件,需要设置编码集，不然可能出现文件为空的情况
                    flag = ftp.retrieveFile(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"), out);
                    // 下载成功删除文件,看项目需求
                    // ftp.deleteFile(new String(fileName.getBytes("UTF-8"),"ISO-8859-1"));
                    out.flush();
                    out.close();
                    if (flag) {
                        logger.info("下载成功");
                    } else {
                        logger.error("下载失败");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("下载失败");
        }

        return flag;
    }

    /**
     * 下载FTP下指定文件
     *
     * @param ftp      FTPClient对象
     * @param filePath FTP文件路径
     * @param fileName 文件名
     * @return
     */
    public static String downLoadBase64(FTPClient ftp, String filePath, String fileName) {
        InputStream inputStream = null;
        ByteArrayOutputStream output = null;
        try {
            // 跳转到文件目录
            ftp.changeWorkingDirectory(new String(filePath.getBytes("UTF-8"), "ISO-8859-1"));
            // 获取目录下文件集合
            ftp.enterLocalPassiveMode();
            FTPFile[] files = ftp.listFiles();
            for (FTPFile file : files) {
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {
                    inputStream = ftp.retrieveFileStream(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    String base64 = Base64.getEncoder().encodeToString(bytes);
                    inputStream.close();
                    return base64;
                }
            }
        } catch (Exception e) {
            logger.error("读取失败");
        } finally {
            try {
                output.close();

                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 下载FTP下指定文件解码
     *
     * @param ftp      FTPClient对象
     * @param filePath FTP文件路径
     * @param fileName 文件名
     * @return
     */
    public static String downLoadCodeBase64(FTPClient ftp, String filePath, String fileName) {
        InputStream inputStream = null;
        ByteArrayOutputStream output = null;
        try {
            // 跳转到文件目录
            ftp.changeWorkingDirectory(new String(filePath.getBytes("UTF-8"), "ISO-8859-1"));
            // 获取目录下文件集合
            ftp.enterLocalPassiveMode();
            FTPFile[] files = ftp.listFiles();
            for (FTPFile file : files) {
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {
                    inputStream = ftp.retrieveFileStream(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
                    //文件解码
                    output = new ByteArrayOutputStream();
                    int n = 0;
                    while ((n = inputStream.read()) != -1) {
                        output.write(n - 1);
                    }
                    inputStream = new ByteArrayInputStream(output.toByteArray());
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    String base64 = Base64.getEncoder().encodeToString(bytes);
                    inputStream.close();
                    return base64;
                }
            }
        } catch (Exception e) {
            logger.error("读取失败");
        } finally {
            try {
                output.close();

                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 下载FTP下指定文件解码
     *
     * @param ftp      FTPClient对象
     * @param filePath FTP文件路径
     * @param fileName 文件名
     * @return
     */
    public static String downLoadSignBase64(FTPClient ftp, String filePath, String fileName,String watermark) {
        InputStream inputStream = null;
        ByteArrayOutputStream output = null;
        try {
            // 跳转到文件目录
            ftp.changeWorkingDirectory(new String(filePath.getBytes("UTF-8"), "ISO-8859-1"));
            // 获取目录下文件集合
            ftp.enterLocalPassiveMode();
            FTPFile[] files = ftp.listFiles();
            for (FTPFile file : files) {
              /*  byte[] bytesname = file.getName().getBytes("iso-8859-1");
                file.setName(new String(bytesname, "utf-8"));*/
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {
                    inputStream = ftp.retrieveFileStream(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
                    //文件解码
                    output = new ByteArrayOutputStream();
                    int n = 0;
                    while ((n = inputStream.read()) != -1) {
                        output.write(n);
                    }
                    inputStream = new ByteArrayInputStream(output.toByteArray());
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    String base64 = Base64.getEncoder().encodeToString(bytes);
                    WaterMark waterMark = new WaterMark();
                   // "data:application/pdf;base64,"
                    if (CommonUtil.isNotEmpty(watermark) && file.getName().contains("pdf")){
                        String pathName = OauthWebConfig.path + watermark + file.getName();
                        waterMark.waterMark(output,pathName, watermark);
                        base64 = Base64Util.encodeBase64File(pathName);
                        File filePdf = new File(pathName);
                        filePdf.delete();
                    }
                    inputStream.close();
                    return base64;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("读取失败");
            throw new CommonException(e);
        } finally {
            try {
                if (output != null) output.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * FTP文件上传
     *
     * @param ftp
     * @param filePath
     * @param ftpPath
     * @return
     */
    public static boolean uploadFile(FTPClient ftp, String filePath, String ftpPath) {
        boolean flag = false;
        InputStream in = null;
        try {
            // 设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if (!ftp.changeWorkingDirectory(ftpPath)) {
                ftp.makeDirectory(ftpPath);
            }
            //跳转目标目录
            ftp.changeWorkingDirectory(ftpPath);
            //上传文件
            File file = new File(filePath);
            in = new FileInputStream(file);
            String tempName = ftpPath + File.separator + file.getName();
            flag = ftp.storeFile(new String(tempName.getBytes("UTF-8"), "ISO-8859-1"), in);
            if (flag) {
                logger.info("上传成功");
            } else {
                logger.error("上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传失败");
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * FTP文件流上传
     *
     * @param ftp
     * @param inputStream
     * @param fileName
     * @param ftpPath
     * @return
     */
    public static boolean uploadInputStream(FTPClient ftp, InputStream inputStream, String ftpPath, String fileName) {
        boolean flag = false;
        ByteArrayOutputStream output = null;
        try {
            // 设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if (!ftp.changeWorkingDirectory(ftpPath)) {
                ftp.makeDirectory(ftpPath);
            }
            //跳转目标目录
            ftp.changeWorkingDirectory(ftpPath);
            //上传文件
            flag = ftp.storeFile(new String(fileName.getBytes("UTF-8"), "ISO-8859-1"), inputStream);
            if (flag) {
                logger.info("上传成功");
            } else {
                logger.error("上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传失败");
        } finally {
            try {
                output.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * FTP文件流编码上传
     *
     * @param ftp
     * @param inputStream
     * @param fileName
     * @param ftpPath
     * @return
     */
    public static boolean uploadCodeInputStream(FTPClient ftp, InputStream inputStream, String ftpPath, String fileName) {
        boolean flag = false;
        ByteArrayOutputStream output = null;
        try {
            // 设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if (!ftp.changeWorkingDirectory(ftpPath)) {
                ftp.makeDirectory(ftpPath);
            }
            //跳转目标目录
            ftp.changeWorkingDirectory(ftpPath);
            //文件加密
            output = new ByteArrayOutputStream();
            int n = 0;
            while ((n = inputStream.read()) != -1) {
                output.write(n + 1);
            }
            inputStream = new ByteArrayInputStream(output.toByteArray());
            //上传文件
            flag = ftp.storeFile(new String(fileName.getBytes("UTF-8"), "ISO-8859-1"), inputStream);
            if (flag) {
                logger.info("上传成功");
            } else {
                logger.error("上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传失败");
        } finally {
            try {
                output.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * FTP文件流签名上传
     *
     * @param ftp
     * @param inputStream
     * @param fileName
     * @param ftpPath
     * @return
     */
    public static boolean uploadSignInputStream(FTPClient ftp, InputStream inputStream, String ftpPath, String fileName) {
        boolean flag = false;
        ByteArrayOutputStream output = null;
        try {
            // 设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if (!ftp.changeWorkingDirectory(new String(ftpPath.getBytes("UTF-8"), "ISO-8859-1"))) {
                ftp.makeDirectory(new String(ftpPath.getBytes("UTF-8"), "ISO-8859-1"));
            }
            //跳转目标目录
            ftp.changeWorkingDirectory(new String(ftpPath.getBytes("UTF-8"), "ISO-8859-1"));
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setControlEncoding("UTF-8");
            //文件加密
            output = new ByteArrayOutputStream();
            int n = 0;
            while ((n = inputStream.read()) != -1) {
                output.write(n);
            }

            //一定要先把上面关闭才会把流加密写入
            inputStream = new ByteArrayInputStream(output.toByteArray());
            fileName = new String(fileName.getBytes("GBK"),"iso-8859-1");
            flag = ftp.storeFile(fileName, inputStream);;
            if (flag) {
                logger.info("上传成功");
            } else {
                logger.error("上传失败");
            }
        } catch (Exception e) {
            logger.error("上传失败");
            e.printStackTrace();
            throw new CommonException(e);
        } finally {
            try {
                if (output != null) output.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * FPT上文件的复制
     *
     * @param ftp      FTPClient对象
     * @param olePath  原文件地址
     * @param newPath  新保存地址
     * @param fileName 文件名
     * @return
     */
    public static boolean copyFile(FTPClient ftp, String olePath, String newPath, String fileName) {
        boolean flag = false;

        try {
            // 跳转到文件目录
            ftp.changeWorkingDirectory(olePath);
            //设置连接模式，不设置会获取为空
            ftp.enterLocalPassiveMode();
            // 获取目录下文件集合
            FTPFile[] files = ftp.listFiles();
            ByteArrayInputStream in = null;
            ByteArrayOutputStream out = null;
            for (FTPFile file : files) {
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {
                    //读取文件，使用下载文件的方法把文件写入内存,绑定到out流上
                    out = new ByteArrayOutputStream();
                    ftp.retrieveFile(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"), out);
                    in = new ByteArrayInputStream(out.toByteArray());
                    // 跳转到文件根目录目录
                    ftp.changeWorkingDirectory("/");
                    //创建新目录
                    ftp.makeDirectory(newPath);
                    //文件复制，先读，再写
                    //二进制
                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                    flag = ftp.storeFile(newPath + File.separator + (new String(file.getName().getBytes("UTF-8"), "ISO-8859-1")), in);
                    out.flush();
                    out.close();
                    in.close();
                    if (flag) {
                        logger.info("转存成功");
                    } else {
                        logger.error("复制失败");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("复制失败");
        }
        return flag;
    }

    /**
     * 实现文件的移动，这里做的是一个文件夹下的所有内容移动到新的文件，
     * 如果要做指定文件移动，加个判断判断文件名
     * 如果不需要移动，只是需要文件重命名，可以使用ftp.rename(oleName,newName)
     *
     * @param ftp
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean moveFile(FTPClient ftp, String oldPath, String newPath) {
        boolean flag = false;

        try {
            ftp.changeWorkingDirectory(oldPath);
            ftp.enterLocalPassiveMode();
            //获取文件数组
            FTPFile[] files = ftp.listFiles();
            //新文件夹不存在则创建
            if (!ftp.changeWorkingDirectory(newPath)) {
                ftp.makeDirectory(newPath);
            }
            //回到原有工作目录
            ftp.changeWorkingDirectory(oldPath);
            for (FTPFile file : files) {
                //转存目录
                flag = ftp.rename(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"), newPath + "/" + new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
                if (flag) {
                    logger.info(file.getName() + "移动成功");
                } else {
                    logger.error(file.getName() + "移动失败");
                }
            }
            //删除文件夹
            if (flag) {
                ftp.removeDirectory(new String(oldPath.getBytes("UTF-8"), "ISO-8859-1"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("移动文件失败");
        }
        return flag;
    }

    /**
     * 删除FTP上指定文件夹下文件及其子文件方法，添加了对中文目录的支持
     *
     * @param ftp       FTPClient对象
     * @param FtpFolder 需要删除的文件夹
     * @return
     */
    public static boolean deleteByFolder(FTPClient ftp, String FtpFolder) {
        boolean flag = false;
        try {
            ftp.changeWorkingDirectory(new String(FtpFolder.getBytes("UTF-8"), "ISO-8859-1"));
            ftp.enterLocalPassiveMode();
            FTPFile[] files = ftp.listFiles();
            for (FTPFile file : files) {
                //判断为文件则删除
                if (file.isFile()) {
                    ftp.deleteFile(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
                }
                //判断是文件夹
                if (file.isDirectory()) {
                    String childPath = FtpFolder + File.separator + file.getName();
                    //递归删除子文件夹
                    deleteByFolder(ftp, new String(childPath.getBytes("UTF-8"), "ISO-8859-1"));
                }
            }
            //循环完成后删除文件夹
            flag = ftp.removeDirectory(new String(FtpFolder.getBytes("UTF-8"), "ISO-8859-1"));
            if (flag) {
                System.out.println(FtpFolder + "文件夹删除成功");
                logger.info(FtpFolder + "文件夹删除成功");
            } else {
                System.out.println(FtpFolder + "文件夹删除失败");
                logger.error(FtpFolder + "文件夹删除失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除失败");
        }
        return flag;
    }

    public static void main(String[] args) {
        String NAME ="637114-2-44487803-3-637114-2-2-0-1-EMR09.00.01-入院记录-1.PDF";
       String[] names= NAME.split("-");

        //tempFile.getName().substring(tempFile.getName().lastIndexOf("-",path.lastIndexOf("")-2)+1,path.lastIndexOf(""))
        System.out.println(names[names.length-2]+"-"+names[names.length-1].substring(0,names[names.length-1].lastIndexOf(".")));
        System.out.println();

    }

    /**
     * 遍历解析文件夹下所有文件
     *
     * @param folderPath 需要解析的的文件夹
     * @param ftp        FTPClient对象
     * @return
     */
    public static boolean readFileByFolder(FTPClient ftp, String folderPath) {
        boolean flage = false;
        try {
            ftp.changeWorkingDirectory(new String(folderPath.getBytes("UTF-8"), "ISO-8859-1"));
            //设置FTP连接模式
            ftp.enterLocalPassiveMode();
            //获取指定目录下文件文件对象集合
            FTPFile files[] = ftp.listFiles();
            InputStream in = null;
            BufferedReader reader = null;
            for (FTPFile file : files) {
                //判断为txt文件则解析
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".txt")) {
                        in = ftp.retrieveFileStream(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
                        reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        String temp;
                        StringBuffer buffer = new StringBuffer();
                        while ((temp = reader.readLine()) != null) {
                            buffer.append(temp);
                        }
                        if (reader != null) {
                            reader.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                        //ftp.retrieveFileStream使用了流，需要释放一下，不然会返回空指针
                        ftp.completePendingCommand();
                        //这里就把一个txt文件完整解析成了个字符串，就可以调用实际需要操作的方法
                        System.out.println(buffer.toString());
                    }
                }
                //判断为文件夹，递归
                if (file.isDirectory()) {
                    String path = folderPath + File.separator + file.getName();
                    readFileByFolder(ftp, path);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文件解析失败");
        }

        return flage;
    }

    /**
     * 判断文件目录下文件是否存在
     *
     * @param ftp      FTPClient对象
     * @param filePath 需要解析的文件夹路径
     * @return
     */
    public static boolean isFTPFileExist(FTPClient ftp, String filePath) {

        InputStream inputStream = null;
        try {
            inputStream = ftp.retrieveFileStream(filePath);
            if (inputStream == null || ftp.getReplyCode() == 550) {
                // 文件不存在
                return false;
            }
            if (inputStream != null) {
                inputStream.close();
                ftp.completePendingCommand();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteFile(FTPClient ftp, String path,String fileName){
        boolean isAppend = false;
        try {
            path = new String(path.getBytes("UTF-8"), "ISO-8859-1");
            ftp.changeWorkingDirectory(path);
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            ftp.dele(fileName);
            logger.info("删除成功");
            ftp.logout();
            isAppend = true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return isAppend;
    }




}
