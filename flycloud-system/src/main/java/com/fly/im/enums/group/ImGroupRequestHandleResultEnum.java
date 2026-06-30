package com.fly.im.enums.group;

import com.fly.im.framework.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * IM 加群申请处理结果枚举
 * <p>
 * 取值与 {@link com.fly.im.enums.friend.ImFriendRequestHandleResultEnum} 平行，便于复用心智模型
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Getter
public enum ImGroupRequestHandleResultEnum implements ArrayValuable<Integer> {

    UNHANDLED(0, "未处理"),
    AGREED(1, "同意"),
    REFUSED(2, "拒绝");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(ImGroupRequestHandleResultEnum::getResult).toArray(Integer[]::new);

    /**
     * 结果
     */
    private final Integer result;
    /**
     * 名字
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    /**
     * 判断申请是否还未处理
     */
    public static boolean isUnhandled(Integer result) {
        return Objects.equals(UNHANDLED.result, result);
    }

}
