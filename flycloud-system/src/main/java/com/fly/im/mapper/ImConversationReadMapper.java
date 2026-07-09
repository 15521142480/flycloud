package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fly.common.security.util.UserUtils;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.system.api.im.domain.ImConversationRead;
import com.fly.system.api.im.domain.vo.ImConversationReadVo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * IM 会话读位置 Mapper
 *
 */
public interface ImConversationReadMapper extends BaseMapperPlus<ImConversationReadMapper, ImConversationRead, ImConversationReadVo> {

    default ImConversationRead selectByUserIdAndConversation(Long userId, Integer conversationType, Long conversationId) {
        return selectOne(new LambdaQueryWrapperX<ImConversationRead>()
                .eq(ImConversationRead::getUserId, userId)
                .eq(ImConversationRead::getConversationType, conversationType)
                .eq(ImConversationRead::getTargetId, conversationId));
    }

    default List<ImConversationRead> selectListByConversation(Integer conversationType, Long conversationId) {
        return selectList(new LambdaQueryWrapperX<ImConversationRead>()
                .eq(ImConversationRead::getConversationType, conversationType)
                .eq(ImConversationRead::getTargetId, conversationId));
    }

    default List<ImConversationRead> selectListByUserIdAndConversations(Long userId, Integer conversationType,
                                                                          Collection<Long> conversationIds) {
        return selectList(new LambdaQueryWrapperX<ImConversationRead>()
                .eq(ImConversationRead::getUserId, userId)
                .eq(ImConversationRead::getConversationType, conversationType)
                .in(ImConversationRead::getTargetId, conversationIds));
    }

    /**
     * 增量拉取当前用户的会话读位置（按 update_time + id 正向游标）
     *
     * @param userId         当前用户编号
     * @param lastUpdateTime 上次拉取到的更新时间；首次拉取传 null
     * @param lastId         上次拉取到的记录编号；首次拉取传 null
     * @param limit          拉取数量
     * @return 会话读位置列表
     */
    default List<ImConversationRead> selectPullListByUserId(Long userId, Long lastUpdateTime, Long lastId, Integer limit) {

        LambdaQueryWrapperX<ImConversationRead> query = new LambdaQueryWrapperX<ImConversationRead>();
        query.and(w -> w.eq(ImConversationRead::getUserId, userId));
        if (lastUpdateTime != null && lastId != null) {
            LocalDateTime lastTime = LocalDateTimeUtil.of(lastUpdateTime);
            query.and(w -> w.gt(ImConversationRead::getUpdateTime, lastTime)
                    .or(n -> n.eq(ImConversationRead::getUpdateTime, lastTime).gt(ImConversationRead::getId, lastId)));
        }
        return selectList(query.orderByAsc(ImConversationRead::getUpdateTime).orderByAsc(ImConversationRead::getId)
                .last("LIMIT " + limit));
    }

    /**
     * 单调递增更新读位置：仅当新位置更大时才更新
     * <p>
     * 通过 {@code read_message_id < messageId} 的 CAS 条件，保证乱序 / 并发上报时读位置不会回退。
     *
     * @param id        记录编号
     * @param messageId 新的最大已读消息编号
     * @param readTime  已读时间
     * @return 影响行数；0 表示新位置不大于已有位置，未更新
     */
    default int updateReadMessageIdToLarger(Long id, Long messageId, LocalDateTime readTime) {
        return update(null, Wrappers.<ImConversationRead>lambdaUpdate()
                .set(ImConversationRead::getMessageId, messageId)
                .set(ImConversationRead::getReadTime, readTime)
                .set(ImConversationRead::getUpdateBy, UserUtils.getCurUserIdStr())
                .set(ImConversationRead::getUpdateTime, LocalDateTime.now())
                .eq(ImConversationRead::getId, id)
                .lt(ImConversationRead::getMessageId, messageId));
    }

}
