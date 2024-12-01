package com.fly.common.enums;

import com.fly.common.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色标识枚举
 */
@Getter
@AllArgsConstructor
public enum RoleCodeEnum {

    SUPER_ADMIN("super_admin", "超级管理员"),
    TENANT_ADMIN("common_admin", "普通管理员"),
    ;

    /**
     * 角色编码
     */
    private final String code;
    /**
     * 名字
     */
    private final String name;


    /**
     * 是否是超级管理员
     *
     * @param code 角色编码
    */
    public static boolean isSuperAdmin(String code) {
        return ObjectUtils.equalsAny(code, SUPER_ADMIN.getCode());
    }

}
