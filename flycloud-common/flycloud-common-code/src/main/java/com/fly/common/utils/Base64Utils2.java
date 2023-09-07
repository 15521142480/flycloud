package com.fly.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Base64 工具类 (jdk11 版)
 *
 * @author lxs
 * @date 2023/4/24
 */
public class Base64Utils2 {

//    public static void main(String[] args) throws Exception {
//        //String base64 = "CmNoYXJzZXRfbWFwICBrb2k4LXIgIHdpbmRvd3MtMTI1MSB7CgogICAgODAgIDg4IDsgIyBldXJvCgogICAgOTUgIDk1IDsgIyBidWxsZXQKCiAgICA5QSAgQTAgOyAjICZuYnNwOwoKICAgIDlFICBCNyA7ICMgJm1pZGRvdDsKCiAgICBBMyAgQjggOyAjIHNtYWxsIHlvCiAgICBBNCAgQkEgOyAjIHNtYWxsIFVrcmFpbmlhbiB5ZQoKICAgIEE2ICBCMyA7ICMgc21hbGwgVWtyYWluaWFuIGkKICAgIEE3ICBCRiA7ICMgc21hbGwgVWtyYWluaWFuIHlpCgogICAgQUQgIEI0IDsgIyBzbWFsbCBVa3JhaW5pYW4gc29mdCBnCiAgICBBRSAgQTIgOyAjIHNtYWxsIEJ5ZWxvcnVzc2lhbiBzaG9ydCB1CgogICAgQjAgIEIwIDsgIyAmZGVnOwoKICAgIEIzICBBOCA7ICMgY2FwaXRhbCBZTwogICAgQjQgIEFBIDsgIyBjYXBpdGFsIFVrcmFpbmlhbiBZRQoKICAgIEI2ICBCMiA7ICMgY2FwaXRhbCBVa3JhaW5pYW4gSQogICAgQjcgIEFGIDsgIyBjYXBpdGFsIFVrcmFpbmlhbiBZSQoKICAgIEI5ICBCOSA7ICMgbnVtZXJvIHNpZ24KCiAgICBCRCAgQTUgOyAjIGNhcGl0YWwgVWtyYWluaWFuIHNvZnQgRwogICAgQkUgIEExIDsgIyBjYXBpdGFsIEJ5ZWxvcnVzc2lhbiBzaG9ydCBVCgogICAgQkYgIEE5IDsgIyAoQykKCiAgICBDMCAgRkUgOyAjIHNtYWxsIHl1CiAgICBDMSAgRTAgOyAjIHNtYWxsIGEKICAgIEMyICBFMSA7ICMgc21hbGwgYgogICAgQzMgIEY2IDsgIyBzbWFsbCB0cwogICAgQzQgIEU0IDsgIyBzbWFsbCBkCiAgICBDNSAgRTUgOyAjIHNtYWxsIHllCiAgICBDNiAgRjQgOyAjIHNtYWxsIGYKICAgIEM3ICBFMyA7ICMgc21hbGwgZwogICAgQzggIEY1IDsgIyBzbWFsbCBraAogICAgQzkgIEU4IDsgIyBzbWFsbCBpCiAgICBDQSAgRTkgOyAjIHNtYWxsIGoKICAgIENCICBFQSA7ICMgc21hbGwgawogICAgQ0MgIEVCIDsgIyBzbWFsbCBsCiAgICBDRCAgRUMgOyAjIHNtYWxsIG0KICAgIENFICBFRCA7ICMgc21hbGwgbgogICAgQ0YgIEVFIDsgIyBzbWFsbCBvCgogICAgRDAgIEVGIDsgIyBzbWFsbCBwCiAgICBEMSAgRkYgOyAjIHNtYWxsIHlhCiAgICBEMiAgRjAgOyAjIHNtYWxsIHIKICAgIEQzICBGMSA7ICMgc21hbGwgcwogICAgRDQgIEYyIDsgIyBzbWFsbCB0CiAgICBENSAgRjMgOyAjIHNtYWxsIHUKICAgIEQ2ICBFNiA7ICMgc21hbGwgemgKICAgIEQ3ICBFMiA7ICMgc21hbGwgdgogICAgRDggIEZDIDsgIyBzbWFsbCBzb2Z0IHNpZ24KICAgIEQ5ICBGQiA7ICMgc21hbGwgeQogICAgREEgIEU3IDsgIyBzbWFsbCB6CiAgICBEQiAgRjggOyAjIHNtYWxsIHNoCiAgICBEQyAgRkQgOyAjIHNtYWxsIGUKICAgIEREICBGOSA7ICMgc21hbGwgc2hjaAogICAgREUgIEY3IDsgIyBzbWFsbCBjaAogICAgREYgIEZBIDsgIyBzbWFsbCBoYXJkIHNpZ24KCiAgICBFMCAgREUgOyAjIGNhcGl0YWwgWVUKICAgIEUxICBDMCA7ICMgY2FwaXRhbCBBCiAgICBFMiAgQzEgOyAjIGNhcGl0YWwgQgogICAgRTMgIEQ2IDsgIyBjYXBpdGFsIFRTCiAgICBFNCAgQzQgOyAjIGNhcGl0YWwgRAogICAgRTUgIEM1IDsgIyBjYXBpdGFsIFlFCiAgICBFNiAgRDQgOyAjIGNhcGl0YWwgRgogICAgRTcgIEMzIDsgIyBjYXBpdGFsIEcKICAgIEU4ICBENSA7ICMgY2FwaXRhbCBLSAogICAgRTkgIEM4IDsgIyBjYXBpdGFsIEkKICAgIEVBICBDOSA7ICMgY2FwaXRhbCBKCiAgICBFQiAgQ0EgOyAjIGNhcGl0YWwgSwogICAgRUMgIENCIDsgIyBjYXBpdGFsIEwKICAgIEVEICBDQyA7ICMgY2FwaXRhbCBNCiAgICBFRSAgQ0QgOyAjIGNhcGl0YWwgTgogICAgRUYgIENFIDsgIyBjYXBpdGFsIE8KCiAgICBGMCAgQ0YgOyAjIGNhcGl0YWwgUAogICAgRjEgIERGIDsgIyBjYXBpdGFsIFlBCiAgICBGMiAgRDAgOyAjIGNhcGl0YWwgUgogICAgRjMgIEQxIDsgIyBjYXBpdGFsIFMKICAgIEY0ICBEMiA7ICMgY2FwaXRhbCBUCiAgICBGNSAgRDMgOyAjIGNhcGl0YWwgVQogICAgRjYgIEM2IDsgIyBjYXBpdGFsIFpICiAgICBGNyAgQzIgOyAjIGNhcGl0YWwgVgogICAgRjggIERDIDsgIyBjYXBpdGFsIHNvZnQgc2lnbgogICAgRjkgIERCIDsgIyBjYXBpdGFsIFkKICAgIEZBICBDNyA7ICMgY2FwaXRhbCBaCiAgICBGQiAgRDggOyAjIGNhcGl0YWwgU0gKICAgIEZDICBERCA7ICMgY2FwaXRhbCBFCiAgICBGRCAgRDkgOyAjIGNhcGl0YWwgU0hDSAogICAgRkUgIEQ3IDsgIyBjYXBpdGFsIENICiAgICBGRiAgREEgOyAjIGNhcGl0YWwgaGFyZCBzaWduCn0K";
//        //System.out.println(Base64ToImg(base64, "E:/work/staticWeb/images/",""));
//        String base = "admin#123456";
//        System.out.println(encode64("admin#admin@123"));
//        System.out.println(decode64("YWRtaW4jMTIzNDU2"));
//    }

    /**
     * 使用Base64对字符串进行编码
     *
     * @param str
     * @return
     */
    public static String encode64(String str) {

        byte[] b = str.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s = new String(b);
        return s;
    }

    /**
     * 使用Base64对字符串进行解码
     *
     * @param str
     * @return
     */
    public static String decode64(String str) {
        byte[] b = str.getBytes();
        Base64 base64 = new Base64();
        b = base64.decode(b);
        String s = new String(b);
        return s;
    }

    /**
     * 图片转base64
     *
     * @param imgPath 图片地址
     * @return
     */
    public static String ImageToBase64(String imgPath) {
        byte[] data = null;
            // 读取图片字节数组
            try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
//        return encoder.encode(Objects.requireNonNull(data));

        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        return encoder.encodeToString(Objects.requireNonNull(data));
    }

    /**
     * 图片base64上传
     *
     * @param imgStr   图片base64
     * @param rootPath 上传路径
     * @param type     文件后缀
     * @return 文件名
     * @throws Exception
     */
    public static String Base64ToImg(String imgStr, String rootPath, String type) throws Exception {
        if (imgStr == null) // 图像数据为空
            return "";

        if (StringUtils.isNotBlank(imgStr))
            imgStr = imgStr.substring(imgStr.indexOf(",") + 1);//去掉base64前缀

        ImageUtils.isExist(rootPath);
        /*File file = new File(rootPath);
        if (file.getParentFile().isDirectory()) {//判断上级目录是否是目录
            if (!file.exists()) {   //如果文件目录不存在
                file.mkdirs();  //创建文件目录
            }
        }*/

//        BASE64Decoder decoder = new BASE64Decoder();
        java.util.Base64.Decoder  decoder = java.util.Base64.getDecoder();

        // Base64解码,对字节数组字符串进行Base64解码并生成图片
        imgStr = imgStr.replaceAll(" ", "+");
        //System.out.println(imgStr);
        //byte[] b = decoder.decodeBuffer(imgStr.replace("data:image/jpeg;base64,", ""));
//        byte[] b = decoder.decodeBuffer(imgStr);
        byte[] b = decoder.decode(imgStr);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {// 调整异常数据
                b[i] += 256;
            }
        }
        String imgName = getRandomFileName();
        if (StringUtils.isNotBlank(type)) {
            imgName += "." + type;
        }
        String imgFilePath = rootPath + imgName;//新生成的图片
        OutputStream out = new FileOutputStream(imgFilePath);
        out.write(b);
        out.flush();
        out.close();

        return imgName;
    }

    public static String getRandomFileName() {

        SimpleDateFormat simpleDateFormat;

        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        Date date = new Date();

        String str = simpleDateFormat.format(date);

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数

        return rannum + str;// 当前时间
    }

    /**
     * 根据http图片地址转变成base64
     *
     * @param url
     * @return
     */
    public static String getBase64ByImgUrl(String url) {
        String suffix = url.substring(url.lastIndexOf(".") + 1);
        try {
            URL urls = new URL(url);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Image image = Toolkit.getDefaultToolkit().getImage(urls);
            BufferedImage biOut = toBufferedImage(image);
            ImageIO.write(biOut, suffix, baos);
            String base64Str = decode(baos.toByteArray());
            return base64Str;
        } catch (Exception e) {
            return "";
        }

    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null),
                    image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    public static String decode(byte[] image) {

//        BASE64Encoder decoder = new BASE64Encoder();
//        return replaceEnter(decoder.encode(image));

        java.util.Base64.Encoder  decoder = java.util.Base64.getEncoder();
        return replaceEnter(decoder.encodeToString(image));
    }

    public static String replaceEnter(String str) {
        String reg = "[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }
}