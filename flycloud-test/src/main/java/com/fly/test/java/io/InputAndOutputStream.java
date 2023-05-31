package com.fly.test.java.io;

import java.io.*;

/**
 * 输入和输出流
 *
 * @author lxs
 * @date 2023/5/31
 */
public class InputAndOutputStream {

    public static void main(String[] args) {

        // 确定需要复制文件的路径和粘贴文件地路径
        String srcFilePath = "/Users/laixueshi/Desktop/redis.conf";
        String destFilePath = "/Users/laixueshi/Desktop/redis2.conf";

        // 创建文件输入流对象与输出流对象
        InputStream input = null;
        OutputStream output = null;

        try {

            // 创建实例对象
            input = new FileInputStream(srcFilePath);
            output = new FileOutputStream(destFilePath);

            // 定义字节数组用于一次性IO流操作的空间大小
            byte[] buffer = new byte[1024];
            // 定义int每次接收字节输入流实际每次读取的空间大小长度
            int len = 0;

            // 使用循环依次读取buffer空间大小的数据
            // 直到read未读取到任何数据返回-1时停止循环,即停止读取
            while ((len = input.read(buffer)) != -1) {
                //将每次读取到的数据输出到字节输出流中指定的数据对象中
                output.write(buffer, 0, len);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

            // 关闭输入流与输出流对象,释放资源
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }


}
