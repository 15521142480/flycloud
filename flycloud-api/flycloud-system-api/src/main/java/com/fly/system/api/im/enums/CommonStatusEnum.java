package com.fly.system.api.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {

    ENABLE(0, "开启"),
    DISABLE(1, "关闭");

    private final Integer status;

    private final String name;

    public static boolean isEnable(Integer status) {
        return ENABLE.status.equals(status);
    }

    public static boolean isDisable(Integer status) {
        return DISABLE.status.equals(status);
    }

}
