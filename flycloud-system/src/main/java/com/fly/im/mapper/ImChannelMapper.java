package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.bo.ImChannelPageBo;
import com.fly.system.api.im.domain.ImChannel;
import com.fly.system.api.im.domain.vo.ImChannelVo;

import java.util.List;

/**
 * IM 频道 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImChannelMapper extends BaseMapperPlus<ImChannelMapper, ImChannel, ImChannelVo> {

    default ImChannel selectByCode(String code) {
        return selectOne(ImChannel::getCode, code);
    }

    default List<ImChannel> selectListByStatusOrderBySort(Integer status) {
        return selectList(new LambdaQueryWrapperX<ImChannel>()
                .eq(ImChannel::getStatus, status)
                .orderByAsc(ImChannel::getSort));
    }

    default PageResult<ImChannel> selectPage(ImChannelPageBo reqVo) {
        return new PageResult<>(selectPage(reqVo.build(), new LambdaQueryWrapperX<ImChannel>()
                .likeIfPresent(ImChannel::getCode, reqVo.getCode())
                .likeIfPresent(ImChannel::getName, reqVo.getName())
                .eqIfPresent(ImChannel::getStatus, reqVo.getStatus())
                .orderByAsc(ImChannel::getSort)
                .orderByDesc(ImChannel::getId)));
    }

}
