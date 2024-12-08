package com.fly.common.enums;

import cn.hutool.core.util.ObjUtil;
import com.fly.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 状态 - 枚举
 *
 * @author lxs
 */
@Getter
@AllArgsConstructor
public enum StatusEnum implements IntArrayValuable {

    ENABLE(0, "开启"),
    DISABLE(1, "关闭"),
    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(StatusEnum::getStatus).toArray();

    // 状态
    final Integer status;

    // 状态名
    final String name;


    @Override
    public int[] array() {
        return ARRAYS;
    }

    /**
     * 根据类型获取名称
     *
    */
    public static String getNameByStatus (int status) {

        String str = "";
        for (StatusEnum enums : StatusEnum.values()) {
            if (enums.getStatus() == status) {
                str = enums.getName();
                break;
            }
        }
        return str;
    }


    public static boolean isEnable(Integer status) {
        return ObjUtil.equal(ENABLE.status, status);
    }

    public static boolean isDisable(Integer status) {
        return ObjUtil.equal(DISABLE.status, status);
    }

}
