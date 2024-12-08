package com.fly.common.enums.bpm;


import com.fly.common.core.IntArrayValuable;
import com.fly.common.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 流程实例 ProcessInstance 的状态
 *
 */
@Getter
@AllArgsConstructor
public enum BpmProcessInstanceStatusEnum implements IntArrayValuable {

    NOT_START(-1, "未开始"),
    RUNNING(1, "审批中"),
    APPROVE(2, "审批通过"),
    REJECT(3, "审批不通过"),
    CANCEL(4, "已取消");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(BpmProcessInstanceStatusEnum::getStatus).toArray();

    /**
     * 状态
     */
    private final Integer status;
    /**
     * 描述
     */
    private final String desc;

    @Override
    public int[] array() {
        return ARRAYS;
    }

    public static boolean isRejectStatus(Integer status) {
        return REJECT.getStatus().equals(status);
    }

    public static boolean isProcessEndStatus(Integer status) {
        return ObjectUtils.equalsAny(status,
                APPROVE.getStatus(), REJECT.getStatus(), CANCEL.getStatus());
    }


}
