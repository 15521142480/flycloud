package com.fly.im.framework.util;

import cn.hutool.extra.pinyin.PinyinUtil;

/**
 * 字符串扩展工具。
 *
 * @author lxs
 * @date 2026-07-02
 */
public class StrUtils {

    public static String toPinyin(String text) {
        return text == null ? null : PinyinUtil.getPinyin(text, "");
    }

}
