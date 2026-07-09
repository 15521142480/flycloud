package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.bo.ImGroupManagerPageBo;
import com.fly.system.api.im.domain.ImGroup;
import com.fly.system.api.im.domain.vo.ImGroupVo;

/**
 * IM 群 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImGroupMapper extends BaseMapperPlus<ImGroupMapper, ImGroup, ImGroupVo> {

    default ImGroup selectByIdForUpdate(Long id) {
        return selectOne(new LambdaQueryWrapperX<ImGroup>()
                .eq(ImGroup::getId, id)
                .last("FOR UPDATE"));
    }

    default PageResult<ImGroup> selectPage(ImGroupManagerPageBo reqVo) {
        return new PageResult<>(selectPage(reqVo.build(), new LambdaQueryWrapperX<ImGroup>()
                .likeIfPresent(ImGroup::getName, reqVo.getName())
                .eqIfPresent(ImGroup::getOwnerUserId, reqVo.getOwnerUserId())
                .eqIfPresent(ImGroup::getStatus, reqVo.getStatus())
                .eqIfPresent(ImGroup::getBanned, reqVo.getBanned())
                .betweenIfPresent(ImGroup::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImGroup::getId)));
    }

}
