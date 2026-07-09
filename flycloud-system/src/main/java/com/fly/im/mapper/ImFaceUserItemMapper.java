package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.bo.ImFaceUserItemManagerPageBo;
import com.fly.system.api.im.domain.ImFaceUserItem;
import com.fly.system.api.im.domain.vo.ImFaceUserItemVo;

import java.util.List;

/**
 * IM 用户私有表情 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImFaceUserItemMapper extends BaseMapperPlus<ImFaceUserItemMapper, ImFaceUserItem, ImFaceUserItemVo> {

    default List<ImFaceUserItem> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<ImFaceUserItem>()
                .eq(ImFaceUserItem::getUserId, userId)
                .orderByAsc(ImFaceUserItem::getSort)
                .orderByDesc(ImFaceUserItem::getId));
    }

    default ImFaceUserItem selectByUserIdAndUrl(Long userId, String url) {
        return selectOne(new LambdaQueryWrapperX<ImFaceUserItem>()
                .eq(ImFaceUserItem::getUserId, userId)
                .eq(ImFaceUserItem::getUrl, url));
    }

    default Long selectCountByUserId(Long userId) {
        return selectCount(ImFaceUserItem::getUserId, userId);
    }

    default PageResult<ImFaceUserItem> selectPage(ImFaceUserItemManagerPageBo reqVo) {
        return new PageResult<>(selectPage(reqVo.build(), new LambdaQueryWrapperX<ImFaceUserItem>()
                .eqIfPresent(ImFaceUserItem::getUserId, reqVo.getUserId())
                .likeIfPresent(ImFaceUserItem::getName, reqVo.getName())
                .betweenIfPresent(ImFaceUserItem::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImFaceUserItem::getId)));
    }

}
