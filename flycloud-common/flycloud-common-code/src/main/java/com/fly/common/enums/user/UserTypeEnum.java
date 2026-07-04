package com.fly.common.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    ADMIN(1),
    MEMBER(2);

    private final Integer value;

}
