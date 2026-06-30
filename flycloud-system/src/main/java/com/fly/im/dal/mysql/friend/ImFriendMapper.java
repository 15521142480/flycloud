package com.fly.im.dal.mysql.friend;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.controller.admin.manager.friend.vo.ImFriendManagerPageReqVo;
import com.fly.im.dal.dataobject.friend.ImFriendDO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * IM 好友关系 Mapper
 *
 * @author lxs
 * @date 2026-06-30
 */
@Mapper
public interface ImFriendMapper extends BaseMapperX<ImFriendDO> {

    default ImFriendDO selectByUserIdAndFriendUserId(Long userId, Long friendUserId) {
        return selectOne(new LambdaQueryWrapperX<ImFriendDO>()
                .eq(ImFriendDO::getUserId, userId)
                .eq(ImFriendDO::getFriendUserId, friendUserId));
    }

    default List<ImFriendDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<ImFriendDO>()
                .eq(ImFriendDO::getUserId, userId)
                .orderByDesc(ImFriendDO::getId));
    }
    default List<ImFriendDO> selectListByUserIdAndStatus(Long userId, Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFriendDO>()
                .eq(ImFriendDO::getUserId, userId)
                .eq(ImFriendDO::getStatus, status)
                .orderByDesc(ImFriendDO::getId));
    }

    default List<ImFriendDO> selectListByUserIdAndFriendUserIdsAndStatus(Long userId,
                                                                        Collection<Long> friendUserIds,
                                                                        Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFriendDO>()
                .eq(ImFriendDO::getUserId, userId)
                .in(ImFriendDO::getFriendUserId, friendUserIds)
                .eq(ImFriendDO::getStatus, status));
    }

    default List<ImFriendDO> selectListByUserIdsAndFriendUserIdAndStatus(Collection<Long> userIds,
                                                                        Long friendUserId,
                                                                        Integer status) {
        return selectList(new LambdaQueryWrapperX<ImFriendDO>()
                .in(ImFriendDO::getUserId, userIds)
                .eq(ImFriendDO::getFriendUserId, friendUserId)
                .eq(ImFriendDO::getStatus, status));
    }

    default PageResult<ImFriendDO> selectPage(ImFriendManagerPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImFriendDO>()
                .eqIfPresent(ImFriendDO::getUserId, reqVo.getUserId())
                .eqIfPresent(ImFriendDO::getFriendUserId, reqVo.getFriendUserId())
                .eqIfPresent(ImFriendDO::getStatus, reqVo.getStatus())
                .eqIfPresent(ImFriendDO::getSilent, reqVo.getSilent())
                .betweenIfPresent(ImFriendDO::getAddTime, reqVo.getAddTime())
                .orderByDesc(ImFriendDO::getId));
    }

    @SuppressWarnings("UnusedReturnValue")
    default int updateReAddFields(Long id, Integer status, LocalDateTime addTime,
                                  Boolean silent, Boolean pinned, Boolean blocked,
                                  String displayName, Integer addSource) {
        return update(null, Wrappers.<ImFriendDO>lambdaUpdate()
                .eq(ImFriendDO::getId, id)
                .set(ImFriendDO::getStatus, status)
                .set(ImFriendDO::getAddTime, addTime)
                .set(ImFriendDO::getSilent, silent)
                .set(ImFriendDO::getPinned, pinned)
                .set(ImFriendDO::getBlocked, blocked)
                .set(ImFriendDO::getDisplayName, displayName)
                .set(ImFriendDO::getAddSource, addSource)
                .set(ImFriendDO::getDeleteTime, null));
    }

}
