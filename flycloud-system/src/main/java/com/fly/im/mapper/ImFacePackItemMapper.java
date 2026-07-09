package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.bo.ImFacePackItemPageBo;
import com.fly.system.api.im.domain.ImFacePackItem;
import com.fly.system.api.im.domain.vo.ImFacePackItemVo;

import java.util.Collection;
import java.util.List;

/**
 * IM 表情包项 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImFacePackItemMapper extends BaseMapperPlus<ImFacePackItemMapper, ImFacePackItem, ImFacePackItemVo> {

    default List<ImFacePackItem> selectListByPackIdsAndStatus(Collection<Long> packIds, Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFacePackItem>()
                .in(ImFacePackItem::getPackId, packIds)
                .eq(ImFacePackItem::getStatus, status)
                .orderByAsc(ImFacePackItem::getPackId)
                .orderByAsc(ImFacePackItem::getSort)
                .orderByAsc(ImFacePackItem::getId));
    }

    default Long selectCountByPackId(Long packId) {
        return selectCount(new LambdaQueryWrapperX<ImFacePackItem>()
                .eq(ImFacePackItem::getPackId, packId));
    }

    default Long selectCountByPackIds(Collection<Long> packIds) {
        return selectCount(new LambdaQueryWrapperX<ImFacePackItem>()
                .in(ImFacePackItem::getPackId, packIds));
    }

    default PageResult<ImFacePackItem> selectPage(ImFacePackItemPageBo reqVo) {
        return new PageResult<>(selectPage(reqVo.build(), new LambdaQueryWrapperX<ImFacePackItem>()
                .eqIfPresent(ImFacePackItem::getPackId, reqVo.getPackId())
                .likeIfPresent(ImFacePackItem::getName, reqVo.getName())
                .eqIfPresent(ImFacePackItem::getStatus, reqVo.getStatus())
                .orderByAsc(ImFacePackItem::getSort)
                .orderByDesc(ImFacePackItem::getId)));
    }

}
