package com.fly.im.framework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    ADMIN(1),
    MEMBER(2);

    private final Integer value;

}
