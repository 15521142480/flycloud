package com.fly.im.framework.mybatis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collection;

/**
 * IM Lambda 查询条件扩展。
 *
 * @author lxs
 * @date 2026-07-02
 */
public class LambdaQueryWrapperX<T> extends LambdaQueryWrapper<T> {

    public LambdaQueryWrapperX<T> likeIfPresent(SFunction<T, ?> column, String value) {
        return StrUtil.isBlank(value) ? this : (LambdaQueryWrapperX<T>) like(column, value);
    }

    public LambdaQueryWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object value) {
        return ObjectUtil.isEmpty(value) ? this : (LambdaQueryWrapperX<T>) eq(column, value);
    }

    public LambdaQueryWrapperX<T> geIfPresent(SFunction<T, ?> column, Object value) {
        return ObjectUtil.isEmpty(value) ? this : (LambdaQueryWrapperX<T>) ge(column, value);
    }

    public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        return CollUtil.isEmpty(values) ? this : (LambdaQueryWrapperX<T>) in(column, values);
    }

    public LambdaQueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object[] values) {
        return ArrayUtil.isEmpty(values) || values.length < 2 || values[0] == null || values[1] == null
                ? this : (LambdaQueryWrapperX<T>) between(column, values[0], values[1]);
    }

}
