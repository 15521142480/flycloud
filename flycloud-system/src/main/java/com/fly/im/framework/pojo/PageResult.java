package com.fly.im.framework.pojo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.utils.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * IM 分页结果适配对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
public class PageResult<T> extends PageVo<T> {

    public PageResult() {
    }

    public PageResult(List<T> list, Long total) {
        super(list, total);
    }

    public PageResult(Page<T> page) {
        super(page.getRecords(), page.getTotal());
        setPages(page.getPages());
    }

    public static <T> PageResult<T> empty(Long total) {
        return new PageResult<>(Collections.emptyList(), total);
    }

    /**
     * 转换分页记录类型，并保留 yudao 分页返回结构。
     *
     * @author lxs
     * @date 2026-07-02
     */
    public static <S, T> PageResult<T> convert(PageVo<S> source, Class<T> targetType) {
        return convert(source, targetType, null);
    }

    /**
     * 转换分页记录类型，并允许调用方补充聚合字段。
     *
     * @author lxs
     * @date 2026-07-02
     */
    public static <S, T> PageResult<T> convert(PageVo<S> source, Class<T> targetType, Consumer<T> peek) {
        if (source == null) {
            return null;
        }
        List<T> list = BeanUtils.toBean(source.getList(), targetType, peek);
        PageResult<T> result = new PageResult<>(list, source.getTotal());
        result.setPages(source.getPages());
        return result;
    }

}
