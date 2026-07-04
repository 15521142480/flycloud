package com.fly.common.enums.bpm;

import cn.hutool.core.util.ArrayUtil;

import com.fly.common.core.ArrayValuable;
import com.fly.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 仿钉钉的流程器设计器条件节点的条件类型
 *
 * @author lxs
 */
@Getter
@AllArgsConstructor
public enum BpmSimpleModeConditionType implements ArrayValuable<Integer> {

    EXPRESSION(1, "条件表达式"),
    RULE(2, "条件规则");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(BpmSimpleModeConditionType::getType).toArray(Integer[]::new);

    private final Integer type;

    private final String name;

    public static BpmSimpleModeConditionType valueOf(Integer type) {
        return ArrayUtil.firstMatch(nodeType -> nodeType.getType().equals(type), values());
    }

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
