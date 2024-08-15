package com.fly.common.utils;

import java.text.DecimalFormat;
import java.util.Properties;

/**
 * 系统工具类
 *
 * @author: lxs
 * @date: 2024/8/15
 */
public class SystemUtils {

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.#");



    /**
     * 获取系统类型
     */
    public static String getSystemType() {

        Properties prop = System.getProperties();
        return prop.getProperty("os.name");
    }

    /**
     * mac系统
     */
    public static boolean isMac() {
        return getSystemType().toLowerCase().contains("mac");
    }

    /**
     * mac系统
     */
    public static boolean isLinux() {
        return getSystemType().toLowerCase().contains("linux");
    }

    /**
     * mac系统
     */
//    public boolean isWindow() {
//        return getSystemType().toLowerCase().contains("window");
//    }


    /**
     * 获取文件大小
     */
    public static String fileSize(long size) {

        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return decimalFormat.format((double) size / (double) gb) + " GB";
        } else if (size >= mb) {
            return decimalFormat.format((double) size / (double) mb) + " MB";
        } else if (size >= kb) {
            return decimalFormat.format((double) size / (double) kb) + " KB";
        } else {
            return size + " B";
        }
    }


}
