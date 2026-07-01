package com.fly.im.dal.mysql.message;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.framework.mybatis.QueryWrapperX;
import com.fly.system.api.im.domain.vo.admin.manager.message.privates.ImPrivateMessageManagerPageReqVo;
import com.fly.system.api.im.domain.message.ImPrivateMessage;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * IM 私聊消息 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
@Mapper
public interface ImPrivateMessageMapper extends BaseMapperX<ImPrivateMessage> {

    /**
     * 根据 minId + 时间窗口增量拉取私聊消息
     *
     * @param userId      当前用户编号
     * @param minId       最小消息 id（不含）
     * @param minSendTime 最早发送时间（不含），限制离线消息时间窗口
     * @param size        拉取数量
     * @return 消息列表
     */
    default List<ImPrivateMessage> selectListByMinId(Long userId, Long minId,
                                                       LocalDateTime minSendTime, Integer size) {
        QueryWrapperX<ImPrivateMessage> wrapper = new QueryWrapperX<>();
        wrapper.and(w -> w.eq("sender_id", userId)
                        .or()
                        .eq("receiver_id", userId))
                .gt("id", minId)
                .gt("send_time", minSendTime)
                .orderByAsc("id");
        wrapper.limitN(size);
        return selectList(wrapper);
    }

    /**
     * 查询私聊历史消息（游标拉取）
     *
     * @param userId     当前用户编号
     * @param receiverId 对方用户编号
     * @param maxId      起始消息 id（不含），为空则从最新开始
     * @param limit      拉取数量
     * @return 消息列表（按 id 倒序）
     */
    default List<ImPrivateMessage> selectHistoryList(Long userId, Long receiverId, Long maxId, Integer limit) {
        QueryWrapperX<ImPrivateMessage> wrapper = new QueryWrapperX<>();
        wrapper.and(w -> w.eq("sender_id", userId).eq("receiver_id", receiverId)
                        .or()
                        .eq("sender_id", receiverId).eq("receiver_id", userId))
                .lt(maxId != null, "id", maxId)
                .orderByDesc("id");
        wrapper.limitN(limit);
        return selectList(wrapper);
    }

    default ImPrivateMessage selectBySenderIdAndClientMessageId(Long senderId, String clientMessageId) {
        return selectOne(new LambdaQueryWrapperX<ImPrivateMessage>()
                .eq(ImPrivateMessage::getSenderId, senderId)
                .eq(ImPrivateMessage::getClientMessageId, clientMessageId));
    }

    default Long selectMaxIdBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, Integer status) {
        ImPrivateMessage message = selectOne(new LambdaQueryWrapperX<ImPrivateMessage>()
                .eq(ImPrivateMessage::getSenderId, senderId)
                .eq(ImPrivateMessage::getReceiverId, receiverId)
                .eq(ImPrivateMessage::getStatus, status)
                .orderByDesc(ImPrivateMessage::getId)
                .last("LIMIT 1"));
        return message != null ? message.getId() : null;
    }

    default int updateBySenderIdAndReceiverIdAndIdLeAndStatus(Long senderId, Long receiverId, Long maxMessageId,
                                                              Integer whereStatus, ImPrivateMessage updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<ImPrivateMessage>()
                .eq(ImPrivateMessage::getSenderId, senderId)
                .eq(ImPrivateMessage::getReceiverId, receiverId)
                .le(ImPrivateMessage::getId, maxMessageId)
                .eq(ImPrivateMessage::getStatus, whereStatus));
    }

    default PageResult<ImPrivateMessage> selectPage(ImPrivateMessageManagerPageReqVo reqVo) {
        LambdaQueryWrapperX<ImPrivateMessage> query = new LambdaQueryWrapperX<>();
        if (reqVo.getSenderId() != null && reqVo.getReceiverId() != null) {
            query.and(w -> w.eq(ImPrivateMessage::getSenderId, reqVo.getSenderId())
                            .eq(ImPrivateMessage::getReceiverId, reqVo.getReceiverId())
                            .or()
                            .eq(ImPrivateMessage::getSenderId, reqVo.getReceiverId())
                            .eq(ImPrivateMessage::getReceiverId, reqVo.getSenderId()));
        } else {
            query.eqIfPresent(ImPrivateMessage::getSenderId, reqVo.getSenderId())
                    .eqIfPresent(ImPrivateMessage::getReceiverId, reqVo.getReceiverId());
        }
        return selectPage(reqVo, query
                .eqIfPresent(ImPrivateMessage::getType, reqVo.getType())
                .likeIfPresent(ImPrivateMessage::getContent, reqVo.getContent())
                .eqIfPresent(ImPrivateMessage::getStatus, reqVo.getStatus())
                .betweenIfPresent(ImPrivateMessage::getSendTime, reqVo.getSendTime())
                .orderByDesc(ImPrivateMessage::getId));
    }

}
