package com.fly.im.framework.util;

import cn.hutool.core.convert.Convert;

/**
 * 数字工具。
 *
 * @author lxs
 * @date 2026-06-30
 */
public class NumberUtils {

    public static Long parseLong(Object value) {
        return Convert.toLong(value);
    }

    public static Integer parseInt(Object value) {
        return Convert.toInt(value);
    }

}
