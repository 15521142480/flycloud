package com.fly.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片工具
 *
 * @author lxs
 * @date 2023/4/24
 */
public class ImageUtils {

    public static void copyFile(String srcPath, String destPath) throws IOException {
        // 打开输入流
        FileInputStream fis = new FileInputStream(srcPath);
        // 打开输出流
        FileOutputStream fos = new FileOutputStream(destPath);

        // 读取和写入信息
        int len = 0;
        while ((len = fis.read()) != -1) {
            fos.write(len);
        }

        // 关闭流  先开后关  后开先关
        fos.close(); // 后开先关
        fis.close(); // 先开后关

    }

    public static boolean isExist(String filePath) {
        filePath = filePath.replace("\\","/");
        String paths[] = filePath.split("/");
        String dir = paths[0];
        for (int i = 0; i < paths.length - 1; i++) {//注意此处循环的长度
            try {
                dir = dir + "/" + paths[i + 1];
                File dirFile = new File(dir);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                    //System.out.println("创建目录为：" + dir);
                }
            } catch (Exception err) {
                //System.err.println("文件夹创建发生异常");
            }
        }
        File fp = new File(filePath);
        if(!fp.exists()){
            return true; // 文件不存在，执行下载功能
        }else{
            return false; // 文件存在不做处理
        }
    }


    public static void main(String[] args) throws Exception {
       /* String str1 = "E:\\work\\staticWeb\\images\\1.jpg";
        String str2 = "E:\\work\\staticWeb\\images\\3.jpg";
        copyFile(str1, str2);*/

       String path = "E:\\work\\staticWeb\\images\\trafficAccidents\\111111\\";
       isExist(path);

    }

}