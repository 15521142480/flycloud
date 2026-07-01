package com.fly.im.service.message;

import com.fly.im.framework.pojo.PageResult;
import com.fly.system.api.im.domain.vo.admin.manager.message.privates.ImPrivateMessageManagerPageReqVo;
import com.fly.system.api.im.domain.vo.admin.message.privates.ImPrivateMessageListReqVo;
import com.fly.system.api.im.domain.vo.admin.message.privates.ImPrivateMessageSendReqVo;
import com.fly.system.api.im.domain.message.ImPrivateMessage;
import com.fly.im.service.message.dto.ImPrivateMessageSendDTO;

import java.util.List;

/**
 * IM 私聊消息 Service 接口
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImPrivateMessageService {

    /**
     * 【用户调用】发送私聊消息
     * <p>
     * 用户在 IM 客户端发送 TEXT / IMAGE 等消息时调用，含幂等、好友校验、敏感词、quote 解析等业务校验。
     * type 校验由 VO 层 {@code @InEnum} + {@code @AssertTrue} 完成（仅允许 normal 类型）。
     *
     * @param senderId 发送人编号
     * @param reqVo    发送请求
     * @return 消息
     */
    ImPrivateMessage sendPrivateMessage(Long senderId, ImPrivateMessageSendReqVo reqVo);

    /**
     * 【系统调用】发送私聊消息
     *
     * @param senderId 发送人编号
     * @param dto      消息 DTO
     * @return 构造的消息 DO（持久化时 id 已回填）
     */
    ImPrivateMessage sendPrivateMessage(Long senderId, ImPrivateMessageSendDTO dto);

    /**
     * 【用户调用】撤回私聊消息
     *
     * @param userId    当前用户编号
     * @param messageId 消息编号
     * @return 撤回后的消息
     */
    ImPrivateMessage recallPrivateMessage(Long userId, Long messageId);

    /**
     * 拉取私聊消息（增量）
     *
     * @param userId 当前用户编号
     * @param minId  最小消息 id（不含）
     * @param size   拉取数量
     * @return 消息列表
     */
    List<ImPrivateMessage> pullPrivateMessageList(Long userId, Long minId, Integer size);

    /**
     * 标记私聊消息已读
     * <p>
     * 语义：将「对方发给当前用户、id <= messageId 的未读消息」一次性翻转为已读，
     * 与群聊 readGroupMessages 对称，避免"select-then-update"两步式带来的竞态。
     *
     * @param userId     当前用户编号
     * @param receiverId 接收方用户编号（对方）
     * @param messageId  已读位置（含），通常是前端会话内最大消息 id
     */
    void readPrivateMessages(Long userId, Long receiverId, Long messageId);

    /**
     * 查询对方已读到我发的最大消息 id
     * <p>
     * 用于多端 / 离线场景下的已读位置补齐：客户端进入会话或断线重连后，
     * 调用此接口拿到对方的 maxReadId，再按 id <= maxReadId 翻转本地自发消息为已读，弥补离线期间错过的 RECEIPT 推送事件。
     *
     * @param userId 当前用户编号
     * @param peerId 对方用户编号
     * @return 对方已读到的最大消息 id；对方一条都没读过时返回 null
     */
    Long getMaxReadMessageId(Long userId, Long peerId);

    /**
     * 查询私聊历史消息（游标拉取）
     *
     * @param userId 当前用户编号
     * @param reqVo  拉取请求
     * @return 消息列表（按 id 倒序）
     */
    List<ImPrivateMessage> getPrivateMessageList(Long userId, ImPrivateMessageListReqVo reqVo);

    // ==================== 管理后台 ====================

    /**
     * 【管理后台】分页查询私聊消息
     */
    PageResult<ImPrivateMessage> getPrivateMessagePage(ImPrivateMessageManagerPageReqVo reqVo);

    /**
     * 【管理后台】获取私聊消息详情
     */
    ImPrivateMessage getPrivateMessage(Long id);

}
