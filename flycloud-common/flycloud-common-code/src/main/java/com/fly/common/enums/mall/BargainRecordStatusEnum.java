package com.fly.common.enums.mall;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商城砍价记录状态枚举。
 */
@Getter
@AllArgsConstructor
public enum BargainRecordStatusEnum implements ArrayValuable<Integer> {

    IN_PROGRESS(1, "砍价中"),
    SUCCESS(2, "砍价成功"),
    FAILURE(3, "砍价失败");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(BargainRecordStatusEnum::getStatus)
            .toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
