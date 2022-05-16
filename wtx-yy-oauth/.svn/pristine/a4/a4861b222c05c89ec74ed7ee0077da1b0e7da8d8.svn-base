package com.kinghis.yyoauth.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.kinghis.yyoauth.common.config.OauthWebConfig;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class WaterMark {


    private static int interval = 0;

    /**
     * 水印方法
     *
     * @param output
     * @param outputFile
     * @param waterMarkName
     */
    public static void waterMark(ByteArrayOutputStream output,
                                 String outputFile, String waterMarkName) {
        try {
            PdfReader reader = new PdfReader(output.toByteArray(), OauthWebConfig.pdfPwd.getBytes());
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                    outputFile));
            addWatermark(stamper, waterMarkName);
            //说三遍
            //一定不要忘记关闭流
            //一定不要忘记关闭流
            //一定不要忘记关闭流
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     * @param logoText 要写入的文字
     * @param srcImgPath 源图片路径
     * @param newImagePath 新图片路径
     * @param degree 旋转角度
     * @param color  字体颜色
     * @param formaName 图片后缀
     */
    public static void markImageByText(String logoText, String srcImgPath,String newImagePath,Integer degree,Color color,String formaName) {
        InputStream is = null;
        OutputStream os = null;
        try {
            // 1、源图片
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),  buffImg.getWidth()/2,buffImg.getHeight() /2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font
            g.setFont(new Font("宋体", Font.BOLD, buffImg.getHeight() /2));
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.15f));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            g.drawString(logoText,  buffImg.getWidth()/2 , buffImg.getHeight()/2);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(newImagePath);
            ImageIO.write(buffImg, formaName, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 水印设置
     *
     * @param pdfStamper
     * @param waterMarkName
     * @throws Exception
     */
    public static void addWatermark(PdfStamper pdfStamper, String waterMarkName) throws Exception {
        PdfContentByte content = null;
        BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                BaseFont.NOT_EMBEDDED);
        Rectangle pageRect = null;
        PdfGState gs = new PdfGState();
        try {
            if (base == null || pdfStamper == null) {
                return;
            }
            // 设置透明度为0.4
            gs.setFillOpacity(0.4f);
            gs.setStrokeOpacity(0.4f);
            int toPage = pdfStamper.getReader().getNumberOfPages();
            JLabel label = new JLabel();
            FontMetrics metrics;
            int textH = 0;
            int textW = 0;
            label.setText(waterMarkName+"|严禁拍照截图");
          /*          <div style="text-align: center;font-size: 32px; font-family: '宋体';color: #FBACAC; opacity: .4;position: absolute;z-index: 99; transform:rotate(-30deg);top: 150px;left: 150px;">
          <p style="border-bottom: #FBACAC solid 2px;">郭晓福/肝脏内科</p>
          <p>严禁拍照截图</p>
        </div>*/
            label.setFont(new Font("宋体",1,24));
            label.setSize(10,10);

            metrics = label.getFontMetrics(label.getFont());
            textH = metrics.getHeight();
            textW = metrics.stringWidth(label.getText());
            for (int i = 1; i <= toPage; i++) {
                pageRect = pdfStamper.getReader().getPageSizeWithRotation(i);
                content = pdfStamper.getOverContent(i);
                content.saveState();
                // set Transparency
                content.setGState(gs);
                content.beginText();
                content.setColorFill(BaseColor.RED);
                content.setFontAndSize(base, 24);

                for (int height = interval + textH; height < pageRect.getHeight();
                     height = height + textH*6) {
                    for (int width = interval + textW; width < pageRect.getWidth() + textW;
                         width = width + textW) {
                        content.showTextAligned(Element.ALIGN_UNDEFINED
                                , waterMarkName, width - textW,
                                height - textH, 30);
                    }
                }
                content.endText();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        // waterMark("C:/Users/86150/Desktop/20180518大图纸测试/A0/PDFtest.pdf", "C:/Users/86150/Desktop/20180518大图纸测试/A0/PDFtestDemo.pdf", "济南炼化分公司");

    }
}
