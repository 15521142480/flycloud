package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fly.common.security.util.UserUtils;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.bo.ImFriendRequestManagerPageBo;
import com.fly.system.api.im.domain.ImFriendRequest;
import com.fly.system.api.im.domain.vo.ImFriendRequestVo;
import com.fly.system.api.im.enums.friend.ImFriendRequestHandleResultEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * IM 好友申请记录 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImFriendRequestMapper extends BaseMapperPlus<ImFriendRequestMapper, ImFriendRequest, ImFriendRequestVo> {

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
                .set(ImFriendRequest::getUpdateBy, UserUtils.getCurUserIdStr())
                .set(ImFriendRequest::getUpdateTime, updateTime)
        );
    }

    default PageResult<ImFriendRequest> selectPage(ImFriendRequestManagerPageBo reqVo) {
        return new PageResult<>(selectPage(reqVo.build(), new LambdaQueryWrapperX<ImFriendRequest>()
                .eqIfPresent(ImFriendRequest::getFromUserId, reqVo.getFromUserId())
                .eqIfPresent(ImFriendRequest::getToUserId, reqVo.getToUserId())
                .eqIfPresent(ImFriendRequest::getHandleResult, reqVo.getHandleResult())
                .eqIfPresent(ImFriendRequest::getAddSource, reqVo.getAddSource())
                .betweenIfPresent(ImFriendRequest::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImFriendRequest::getId)));
    }

    /**
     * 增量拉取「我相关」的好友申请（双向 OR，按 update_time + id 正向游标）
     *
     * @param userId         当前用户编号
     * @param lastUpdateTime 上次拉取到的更新时间；首次拉取传 null
     * @param lastId         上次拉取到的记录编号；首次拉取传 null
     * @param limit          拉取数量
     * @return 好友申请列表
     */
    default List<ImFriendRequest> selectPullListByUserId(Long userId, Long lastUpdateTime, Long lastId, Integer limit) {

        LambdaQueryWrapperX<ImFriendRequest> query = new LambdaQueryWrapperX<>();
        query.and(w -> w.eq(ImFriendRequest::getFromUserId, userId)
                .or().eq(ImFriendRequest::getToUserId, userId));

        if (lastUpdateTime != null && lastId != null) {
            LocalDateTime lastTime = LocalDateTimeUtil.of(lastUpdateTime);
            query.and(w -> w.gt(ImFriendRequest::getUpdateTime, lastTime)
                    .or(n -> n.eq(ImFriendRequest::getUpdateTime, lastTime).gt(ImFriendRequest::getId, lastId)));
        }

        return selectList(query.orderByAsc(ImFriendRequest::getUpdateTime).orderByAsc(ImFriendRequest::getId)
                .last("LIMIT " + limit));
    }

}
