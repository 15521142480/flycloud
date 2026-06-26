package com.fly.common.utils.validation;

import com.fly.common.exception.BusinessException;

import java.util.function.Function;

/**
 * 必填数据验证
 *
 * @author: lxs
 * @date: 2026/6/26
 */
public class RequireUtils {


    /**
     * 必填验证数据
     */
    public static <T> T requireById(Function<Long, T> loader, Long id, String message) {

        if (id == null) {
            throw new BusinessException(message);
        }
        T value = loader.apply(id);
        if (value == null) {
            throw new BusinessException(message);
        }
        return value;
    }

}
