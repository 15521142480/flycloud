package com.fly.common.enums.mall;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商城优惠券状态枚举。
 */
@Getter
@AllArgsConstructor
public enum CouponStatusEnum implements ArrayValuable<Integer> {

    UNUSED(1, "未使用"),
    USED(2, "已使用"),
    EXPIRED(3, "已过期");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(CouponStatusEnum::getStatus)
            .toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
