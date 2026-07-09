package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.bo.ImRtcCallManagerPageBo;
import com.fly.system.api.im.domain.ImRtcCall;
import com.fly.system.api.im.domain.vo.ImRtcCallVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * IM 通话记录 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImRtcCallMapper extends BaseMapperPlus<ImRtcCallMapper, ImRtcCall, ImRtcCallVo> {

    default ImRtcCall selectByRoom(String room) {
        return selectOne(ImRtcCall::getRoom, room);
    }

    default ImRtcCall selectLastOneByGroupIdAndStatusIn(Long groupId, Collection<Integer> statuses) {
        return selectLastOne(new LambdaQueryWrapperX<ImRtcCall>()
                .eq(ImRtcCall::getGroupId, groupId)
                .in(ImRtcCall::getStatus, statuses));
    }

    default int updateByIdAndStatus(Long id, Integer oldStatus, ImRtcCall updateObj) {
        return update(updateObj, Wrappers.<ImRtcCall>lambdaUpdate()
                .eq(ImRtcCall::getId, id)
                .eq(ImRtcCall::getStatus, oldStatus));
    }

    @SuppressWarnings("UnusedReturnValue")
    default int updateByIdAndStatusIn(Long id, Collection<Integer> statuses, ImRtcCall updateObj) {
        return update(updateObj, Wrappers.<ImRtcCall>lambdaUpdate()
                .eq(ImRtcCall::getId, id)
                .in(ImRtcCall::getStatus, statuses));
    }

    default List<ImRtcCall> selectListByStatusInAndStartTimeBefore(Collection<Integer> statuses,
                                                                    LocalDateTime startTimeBefore) {
        return selectList(new LambdaQueryWrapperX<ImRtcCall>()
                .in(ImRtcCall::getStatus, statuses)
                .lt(ImRtcCall::getStartTime, startTimeBefore));
    }

    default PageResult<ImRtcCall> selectPage(ImRtcCallManagerPageBo reqVo) {
        return new PageResult<>(selectPage(reqVo.build(), new LambdaQueryWrapperX<ImRtcCall>()
                .eqIfPresent(ImRtcCall::getInviterUserId, reqVo.getInviterUserId())
                .eqIfPresent(ImRtcCall::getConversationType, reqVo.getConversationType())
                .eqIfPresent(ImRtcCall::getMediaType, reqVo.getMediaType())
                .eqIfPresent(ImRtcCall::getStatus, reqVo.getStatus())
                .eqIfPresent(ImRtcCall::getEndReason, reqVo.getEndReason())
                .betweenIfPresent(ImRtcCall::getStartTime, reqVo.getStartTime())
                .orderByDesc(ImRtcCall::getId)));
    }

}
