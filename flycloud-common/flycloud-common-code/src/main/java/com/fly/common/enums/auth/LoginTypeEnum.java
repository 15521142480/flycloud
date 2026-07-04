package com.fly.common.enums.auth;

import cn.hutool.core.util.ArrayUtil;
import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 登录类型枚举
 *
 * @author lxs
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum implements ArrayValuable<Integer> {


    ADMIN_ACCOUNT_PASSWORD(1, "admin_帐号密码登录"),

    APP_MOBILE_PASSWORD(11, "app_手机密码登录"),
    APP_SMS(12, "app_手机验证码登录"),

    ;



    public static final Integer[] ARRAYS = Arrays.stream(values()).map(LoginTypeEnum::getType).toArray(Integer[]::new);


    /**
     * 类型
     */
    private final Integer type;
    /**
     * 类型的标识
     */
    private final String source;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static LoginTypeEnum valueOfType(Integer type) {
        return ArrayUtil.firstMatch(o -> o.getType().equals(type), values());
    }

}
