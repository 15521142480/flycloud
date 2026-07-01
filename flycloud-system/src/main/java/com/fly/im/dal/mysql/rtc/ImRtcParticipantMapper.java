package com.fly.im.dal.mysql.rtc;

import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.rtc.ImRtcParticipant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * IM 通话参与者 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
@Mapper
public interface ImRtcParticipantMapper extends BaseMapperX<ImRtcParticipant> {

    default ImRtcParticipant selectByRoomAndUserId(String room, Long userId) {
        return selectOne(new LambdaQueryWrapperX<ImRtcParticipant>()
                .eq(ImRtcParticipant::getRoom, room)
                .eq(ImRtcParticipant::getUserId, userId));
    }

    default List<ImRtcParticipant> selectListByRoom(String room) {
        return selectList(ImRtcParticipant::getRoom, room);
    }

    default List<ImRtcParticipant> selectListByCallId(Long callId) {
        return selectList(ImRtcParticipant::getCallId, callId);
    }

    default List<ImRtcParticipant> selectListByStatusAndInviteTimeBefore(Integer status, LocalDateTime threshold) {
        return selectList(new LambdaQueryWrapperX<ImRtcParticipant>()
                .eq(ImRtcParticipant::getStatus, status)
                .lt(ImRtcParticipant::getInviteTime, threshold));
    }

    default List<ImRtcParticipant> selectListByRoomAndStatusAndInviteTimeBefore(String room, Integer status, LocalDateTime threshold) {
        return selectList(new LambdaQueryWrapperX<ImRtcParticipant>()
                .eq(ImRtcParticipant::getRoom, room)
                .eq(ImRtcParticipant::getStatus, status)
                .lt(ImRtcParticipant::getInviteTime, threshold));
    }

    default ImRtcParticipant selectLastOneByUserIdAndStatus(Long userId, Collection<Integer> statuses) {
        return selectLastOne(new LambdaQueryWrapperX<ImRtcParticipant>()
                .eq(ImRtcParticipant::getUserId, userId)
                .in(ImRtcParticipant::getStatus, statuses));
    }

    default ImRtcParticipant selectLastOneByUserIdAndStatusInAndRoomNot(Long userId, Collection<Integer> statuses, String room) {
        return selectLastOne(new LambdaQueryWrapperX<ImRtcParticipant>()
                .eq(ImRtcParticipant::getUserId, userId)
                .in(ImRtcParticipant::getStatus, statuses)
                .ne(ImRtcParticipant::getRoom, room));
    }

    @SuppressWarnings("UnusedReturnValue")
    default int updateByIdAndStatus(Long id, Integer oldStatus, ImRtcParticipant updateObj) {
        return update(updateObj, Wrappers.<ImRtcParticipant>lambdaUpdate()
                .eq(ImRtcParticipant::getId, id)
                .eq(ImRtcParticipant::getStatus, oldStatus));
    }

    @SuppressWarnings("UnusedReturnValue")
    default int updateByRoomAndStatus(String room, Integer oldStatus, ImRtcParticipant updateObj) {
        return update(updateObj, Wrappers.<ImRtcParticipant>lambdaUpdate()
                .eq(ImRtcParticipant::getRoom, room)
                .eq(ImRtcParticipant::getStatus, oldStatus));
    }

}
