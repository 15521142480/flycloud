package com.fly.im.service.websocket;

import com.fly.im.service.websocket.dto.ImChannelMessageDTO;
import com.fly.im.service.websocket.dto.ImGroupMessageDTO;
import com.fly.im.service.websocket.dto.ImPrivateMessageDTO;

import java.util.Collection;
import java.util.Collections;

/**
 * IM WebSocket 推送 Service 接口
 * <p>
 * 统一封装 WebSocket 消息推送，事务内调用时提交后异步执行。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImWebSocketService {

    /**
     * 异步推送不绑定具体消息 DTO 的 IM 通知给指定用户。
     *
     * @param userId           目标用户编号
     * @param conversationType 会话类型；系统通知使用 {@code NONE}
     * @param contentType      内容类型
     * @param payload          通知内容
     */
    default void sendNotificationAsync(Long userId, Integer conversationType, Integer contentType, Object payload) {
        sendNotificationAsync(Collections.singleton(userId), conversationType, contentType, payload);
    }

    /**
     * 异步批量推送不绑定具体消息 DTO 的 IM 通知。
     *
     * 好友申请、入群申请和 RTC 信令等状态事件不属于聊天消息，调用方可传入
     * {@code ImConversationTypeEnum.NONE}，前端将其分发到对应业务 Store。
     *
     * @param userIds          目标用户编号列表
     * @param conversationType 会话类型
     * @param contentType      内容类型
     * @param payload          通知内容
     */
    void sendNotificationAsync(Collection<Long> userIds, Integer conversationType, Integer contentType, Object payload);

    /**
     * 异步广播不绑定具体消息 DTO 的 IM 通知。
     *
     * @param conversationType 会话类型
     * @param contentType      内容类型
     * @param payload          通知内容
     */
    void broadcastNotificationAsync(Integer conversationType, Integer contentType, Object payload);

    /**
     * 异步推送私聊消息给指定用户
     *
     * @param userId 目标用户编号
     * @param dto    私聊消息 DTO
     */
    default void sendPrivateMessageAsync(Long userId, ImPrivateMessageDTO dto) {
        sendPrivateMessageAsync(Collections.singleton(userId), dto);
    }

    /**
     * 异步批量推送私聊消息给多个用户；用于同一份 DTO 扇出到多个收件人
     * <p>
     * 相比逐个发送，仅注册一次 afterCommit 回调和异步任务。
     *
     * @param userIds 目标用户编号列表
     * @param dto     私聊消息 DTO
     */
    void sendPrivateMessageAsync(Collection<Long> userIds, ImPrivateMessageDTO dto);

    /**
     * 异步推送群聊消息给指定用户
     *
     * @param userId 目标用户编号
     * @param dto    群聊消息 DTO
     */
    default void sendGroupMessageAsync(Long userId, ImGroupMessageDTO dto) {
        sendGroupMessageAsync(Collections.singleton(userId), dto);
    }

    /**
     * 异步批量推送群聊消息给多个用户
     *
     * @param userIds 目标用户编号列表
     * @param dto     群聊消息 DTO
     */
    void sendGroupMessageAsync(Collection<Long> userIds, ImGroupMessageDTO dto);

    /**
     * 异步推送频道消息给指定用户
     *
     * @param userId 目标用户编号
     * @param dto    频道消息 DTO
     */
    default void sendChannelMessageAsync(Long userId, ImChannelMessageDTO dto) {
        sendChannelMessageAsync(Collections.singleton(userId), dto);
    }

    /**
     * 异步批量推送频道消息给多个用户
     *
     * @param userIds 目标用户编号列表
     * @param dto     频道消息 DTO
     */
    void sendChannelMessageAsync(Collection<Long> userIds, ImChannelMessageDTO dto);

    /**
     * 异步广播频道消息给当前所有在线管理端用户；用于全员推送
     *
     * @param dto 频道消息 DTO
     */
    void broadcastChannelMessageAsync(ImChannelMessageDTO dto);

}
