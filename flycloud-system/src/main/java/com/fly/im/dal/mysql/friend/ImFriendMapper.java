package com.fly.im.dal.mysql.friend;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.vo.admin.manager.friend.ImFriendManagerPageReqVo;
import com.fly.system.api.im.domain.friend.ImFriend;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * IM 好友关系 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
@Mapper
public interface ImFriendMapper extends BaseMapperX<ImFriend> {

    default ImFriend selectByUserIdAndFriendUserId(Long userId, Long friendUserId) {
        return selectOne(new LambdaQueryWrapperX<ImFriend>()
                .eq(ImFriend::getUserId, userId)
                .eq(ImFriend::getFriendUserId, friendUserId));
    }

    default List<ImFriend> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<ImFriend>()
                .eq(ImFriend::getUserId, userId)
                .orderByDesc(ImFriend::getId));
    }
    default List<ImFriend> selectListByUserIdAndStatus(Long userId, Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFriend>()
                .eq(ImFriend::getUserId, userId)
                .eq(ImFriend::getStatus, status)
                .orderByDesc(ImFriend::getId));
    }

    default List<ImFriend> selectListByUserIdAndFriendUserIdsAndStatus(Long userId,
                                                                        Collection<Long> friendUserIds,
                                                                        Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFriend>()
                .eq(ImFriend::getUserId, userId)
                .in(ImFriend::getFriendUserId, friendUserIds)
                .eq(ImFriend::getStatus, status));
    }

    default List<ImFriend> selectListByUserIdsAndFriendUserIdAndStatus(Collection<Long> userIds,
                                                                        Long friendUserId,
                                                                        Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFriend>()
                .in(ImFriend::getUserId, userIds)
                .eq(ImFriend::getFriendUserId, friendUserId)
                .eq(ImFriend::getStatus, status));
    }

    default PageResult<ImFriend> selectPage(ImFriendManagerPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImFriend>()
                .eqIfPresent(ImFriend::getUserId, reqVo.getUserId())
                .eqIfPresent(ImFriend::getFriendUserId, reqVo.getFriendUserId())
                .eqIfPresent(ImFriend::getStatus, reqVo.getStatus())
                .eqIfPresent(ImFriend::getSilent, reqVo.getSilent())
                .betweenIfPresent(ImFriend::getAddTime, reqVo.getAddTime())
                .orderByDesc(ImFriend::getId));
    }

    @SuppressWarnings("UnusedReturnValue")
    default int updateReAddFields(Long id, Integer status, LocalDateTime addTime,
                                  Boolean silent, Boolean pinned, Boolean blocked,
                                  String displayName, Integer addSource) {
        return update(null, Wrappers.<ImFriend>lambdaUpdate()
                .eq(ImFriend::getId, id)
                .set(ImFriend::getStatus, status)
                .set(ImFriend::getAddTime, addTime)
                .set(ImFriend::getSilent, silent)
                .set(ImFriend::getPinned, pinned)
                .set(ImFriend::getBlocked, blocked)
                .set(ImFriend::getDisplayName, displayName)
                .set(ImFriend::getAddSource, addSource)
                .set(ImFriend::getDeleteTime, null));
    }

}
