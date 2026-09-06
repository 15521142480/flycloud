package com.fly.common.enums.mall;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商城拼团记录状态枚举。
 */
@Getter
@AllArgsConstructor
public enum CombinationRecordStatusEnum implements ArrayValuable<Integer> {

    IN_PROGRESS(1, "拼团中"),
    SUCCESS(2, "拼团成功"),
    FAILURE(3, "拼团失败");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(CombinationRecordStatusEnum::getStatus)
            .toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
