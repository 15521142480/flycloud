package com.fly.im.framework.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.common.domain.bo.PageBo;
import com.fly.im.framework.pojo.PageResult;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * IM Mapper 扩展接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface BaseMapperX<T> extends BaseMapperPlus<BaseMapperX<T>, T, T> {

    /**
     * 根据字段查询单条记录。
     */
    default <V> T selectOne(SFunction<T, ?> field, V value) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * 根据字段查询列表。
     */
    default <V> List<T> selectList(SFunction<T, ?> field, V value) {
        return selectList(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * 根据字段统计数量。
     */
    default <V> Long selectCount(SFunction<T, ?> field, V value) {
        return selectCount(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * 根据主键批量查询。
     */
    default List<T> selectByIds(Collection<? extends Serializable> ids) {
        return selectBatchIds(ids);
    }

    /**
     * 查询最后一条记录。
     */
    default T selectLastOne(LambdaQueryWrapper<T> wrapper) {
        wrapper.last("LIMIT 1");
        return selectOne(wrapper);
    }

    /**
     * 分页查询。
     */
    default PageResult<T> selectPage(PageBo pageBo, Wrapper<T> wrapper) {
        Page<T> page = selectPage(pageBo.build(), wrapper);
        return new PageResult<>(page);
    }

}
