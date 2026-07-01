package com.fly.im.dal.mysql.friend;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.vo.admin.manager.friend.ImFriendRequestManagerPageReqVo;
import com.fly.system.api.im.domain.friend.ImFriendRequest;
import com.fly.system.api.im.enums.friend.ImFriendRequestHandleResultEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * IM 好友申请记录 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
@Mapper
public interface ImFriendRequestMapper extends BaseMapperX<ImFriendRequest> {

    default ImFriendRequest selectByFromUserIdAndToUserId(Long fromUserId, Long toUserId) {
        return selectOne(new LambdaQueryWrapperX<ImFriendRequest>()
                .eq(ImFriendRequest::getFromUserId, fromUserId)
                .eq(ImFriendRequest::getToUserId, toUserId));
    }

    /**
     * 拉取「我相关」的好友申请列表
     */
    default List<ImFriendRequest> selectMyList(Long userId, LocalDateTime maxRequestUpdateTime,
                                                 Long maxId, int limit) {
        LambdaQueryWrapperX<ImFriendRequest> wrapper = new LambdaQueryWrapperX<>();
        wrapper.and(w -> w.eq(ImFriendRequest::getFromUserId, userId)
                        .or().eq(ImFriendRequest::getToUserId, userId));
        if (maxRequestUpdateTime != null && maxId != null) {
            wrapper.and(w -> w.lt(ImFriendRequest::getUpdateTime, maxRequestUpdateTime)
                    .or(n -> n.eq(ImFriendRequest::getUpdateTime, maxRequestUpdateTime)
                            .lt(ImFriendRequest::getId, maxId)));
        }
        wrapper.orderByDesc(ImFriendRequest::getUpdateTime)
                .orderByDesc(ImFriendRequest::getId)
                .last("LIMIT " + limit);
        return selectList(wrapper);
    }

    default int updateByIdAndHandleResult(Long id, Integer handleResult, ImFriendRequest updateObj) {
        return update(updateObj, new LambdaUpdateWrapper<ImFriendRequest>()
                .eq(ImFriendRequest::getId, id).eq(ImFriendRequest::getHandleResult, handleResult));
    }

    /**
     * 复用 (fromUserId, toUserId) 旧申请记录：覆盖申请理由 / 备注 / 来源，重置为未处理 + 清空旧处理痕迹
     * <p>
     * handleContent / handleTime 走 LambdaUpdateWrapper.set 显式置 null，updateById 默认会忽略 null 字段
     */
    default int updateByIdReset(Long id, String applyContent, String displayName, Integer addSource,
                                LocalDateTime updateTime) {
        return update(null, new LambdaUpdateWrapper<ImFriendRequest>()
                .eq(ImFriendRequest::getId, id)
                .set(ImFriendRequest::getApplyContent, applyContent)
                .set(ImFriendRequest::getDisplayName, displayName)
                .set(ImFriendRequest::getAddSource, addSource)
                .set(ImFriendRequest::getHandleResult, ImFriendRequestHandleResultEnum.UNHANDLED.getResult())
                .set(ImFriendRequest::getHandleContent, null)
                .set(ImFriendRequest::getHandleTime, null)
                .set(ImFriendRequest::getUpdateTime, updateTime));
    }

    default PageResult<ImFriendRequest> selectPage(ImFriendRequestManagerPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImFriendRequest>()
                .eqIfPresent(ImFriendRequest::getFromUserId, reqVo.getFromUserId())
                .eqIfPresent(ImFriendRequest::getToUserId, reqVo.getToUserId())
                .eqIfPresent(ImFriendRequest::getHandleResult, reqVo.getHandleResult())
                .eqIfPresent(ImFriendRequest::getAddSource, reqVo.getAddSource())
                .betweenIfPresent(ImFriendRequest::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImFriendRequest::getId));
    }

}
