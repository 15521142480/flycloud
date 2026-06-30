package com.fly.im.dal.mysql.message;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.framework.mybatis.QueryWrapperX;
import com.fly.im.controller.admin.manager.message.vo.privates.ImPrivateMessageManagerPageReqVo;
import com.fly.im.dal.dataobject.message.ImPrivateMessageDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * IM 私聊消息 Mapper
 *
 * @author lxs
 * @date 2026-06-30
 */
@Mapper
public interface ImPrivateMessageMapper extends BaseMapperX<ImPrivateMessageDO> {

    /**
     * 根据 minId + 时间窗口增量拉取私聊消息
     *
     * @param userId      当前用户编号
     * @param minId       最小消息 id（不含）
     * @param minSendTime 最早发送时间（不含），限制离线消息时间窗口
     * @param size        拉取数量
     * @return 消息列表
     */
    default List<ImPrivateMessageDO> selectListByMinId(Long userId, Long minId,
                                                       LocalDateTime minSendTime, Integer size) {
        QueryWrapperX<ImPrivateMessageDO> wrapper = new QueryWrapperX<>();
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
    default List<ImPrivateMessageDO> selectHistoryList(Long userId, Long receiverId, Long maxId, Integer limit) {
        QueryWrapperX<ImPrivateMessageDO> wrapper = new QueryWrapperX<>();
        wrapper.and(w -> w.eq("sender_id", userId).eq("receiver_id", receiverId)
                        .or()
                        .eq("sender_id", receiverId).eq("receiver_id", userId))
                .lt(maxId != null, "id", maxId)
                .orderByDesc("id");
        wrapper.limitN(limit);
        return selectList(wrapper);
    }

    default ImPrivateMessageDO selectBySenderIdAndClientMessageId(Long senderId, String clientMessageId) {
        return selectOne(new LambdaQueryWrapperX<ImPrivateMessageDO>()
                .eq(ImPrivateMessageDO::getSenderId, senderId)
                .eq(ImPrivateMessageDO::getClientMessageId, clientMessageId));
    }

    default Long selectMaxIdBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, Integer status) {
        ImPrivateMessageDO message = selectOne(new LambdaQueryWrapperX<ImPrivateMessageDO>()
                .eq(ImPrivateMessageDO::getSenderId, senderId)
                .eq(ImPrivateMessageDO::getReceiverId, receiverId)
                .eq(ImPrivateMessageDO::getStatus, status)
                .orderByDesc(ImPrivateMessageDO::getId)
                .last("LIMIT 1"));
        return message != null ? message.getId() : null;
    }

    default int updateBySenderIdAndReceiverIdAndIdLeAndStatus(Long senderId, Long receiverId, Long maxMessageId,
                                                              Integer whereStatus, ImPrivateMessageDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<ImPrivateMessageDO>()
                .eq(ImPrivateMessageDO::getSenderId, senderId)
                .eq(ImPrivateMessageDO::getReceiverId, receiverId)
                .le(ImPrivateMessageDO::getId, maxMessageId)
                .eq(ImPrivateMessageDO::getStatus, whereStatus));
    }

    default PageResult<ImPrivateMessageDO> selectPage(ImPrivateMessageManagerPageReqVo reqVo) {
        LambdaQueryWrapperX<ImPrivateMessageDO> query = new LambdaQueryWrapperX<>();
        if (reqVo.getSenderId() != null && reqVo.getReceiverId() != null) {
            query.and(w -> w.eq(ImPrivateMessageDO::getSenderId, reqVo.getSenderId())
                            .eq(ImPrivateMessageDO::getReceiverId, reqVo.getReceiverId())
                            .or()
                            .eq(ImPrivateMessageDO::getSenderId, reqVo.getReceiverId())
                            .eq(ImPrivateMessageDO::getReceiverId, reqVo.getSenderId()));
        } else {
            query.eqIfPresent(ImPrivateMessageDO::getSenderId, reqVo.getSenderId())
                    .eqIfPresent(ImPrivateMessageDO::getReceiverId, reqVo.getReceiverId());
        }
        return selectPage(reqVo, query
                .eqIfPresent(ImPrivateMessageDO::getType, reqVo.getType())
                .likeIfPresent(ImPrivateMessageDO::getContent, reqVo.getContent())
                .eqIfPresent(ImPrivateMessageDO::getStatus, reqVo.getStatus())
                .betweenIfPresent(ImPrivateMessageDO::getSendTime, reqVo.getSendTime())
                .orderByDesc(ImPrivateMessageDO::getId));
    }

}
