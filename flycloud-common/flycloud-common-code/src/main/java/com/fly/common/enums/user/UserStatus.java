package com.fly.common.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态-枚举
 *
 * @author lxs
 * @date 2023/5/2
 */
@AllArgsConstructor
@Getter
public enum UserStatus {


    ENABLE("0", "正常"),
    DISABLE ("0", "停用");


    private final String value;
    private final String label;


}
