package com.fly.common.utils;

import java.util.UUID;

/**
 * UUID工具类
 */
public class UUIDUtils {

    /**
     * 获取UUID
     */
    public static String generateUUID() {

        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-","");
    }
}
