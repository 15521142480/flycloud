package com.fly.im.framework.mybatis;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * IM QueryWrapper 扩展。
 *
 * @author lxs
 * @date 2026-06-30
 */
public class QueryWrapperX<T> extends QueryWrapper<T> {

    public QueryWrapperX<T> likeIfPresent(String column, String value) {
        return StrUtil.isBlank(value) ? this : (QueryWrapperX<T>) like(column, value);
    }

    public QueryWrapperX<T> limitN(Integer limit) {
        if (limit != null && limit > 0) {
            last("LIMIT " + limit);
        }
        return this;
    }

}
