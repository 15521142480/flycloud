package com.fly.im.service.websocket;

import com.fly.common.enums.user.UserTypeEnum;
import com.fly.im.service.websocket.bo.ImNotificationWebSocketBo;
import com.fly.im.service.websocket.dto.ImChannelMessageDTO;
import com.fly.im.service.websocket.dto.ImGroupMessageDTO;
import com.fly.im.service.websocket.dto.ImPrivateMessageDTO;
import com.fly.system.api.im.enums.ImConversationTypeEnum;
import com.fly.system.api.websocket.feign.WebSocketSenderApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * IM WebSocket 推送 Service 实现类
 * <p>
 * 当调用方处于事务中时，推送会延迟到事务提交后再异步执行，
 * 避免客户端收到 WebSocket 消息时数据库变更尚未可见。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Service
@Validated
@Slf4j
public class ImWebSocketServiceImpl implements ImWebSocketService {

    @Resource
    private WebSocketSenderApi webSocketSenderApi;

    @Override
    public void sendNotificationAsync(Collection<Long> userIds, Integer conversationType, Integer contentType,
                                      Object payload) {
        ImNotificationWebSocketBo notification = buildNotification(conversationType, contentType, payload);
        executeAfterTransaction(() -> doSendNotification(userIds, notification));
    }

    @Override
    public void broadcastNotificationAsync(Integer conversationType, Integer contentType, Object payload) {
        ImNotificationWebSocketBo notification = buildNotification(conversationType, contentType, payload);
        executeAfterTransaction(() -> doBroadcastNotification(notification));
    }

    @Override
    public void sendPrivateMessageAsync(Collection<Long> userIds, ImPrivateMessageDTO dto) {
        sendNotificationAsync(userIds, ImConversationTypeEnum.PRIVATE.getType(), dto.getType(), dto);
    }

    @Override
    public void sendGroupMessageAsync(Collection<Long> userIds, ImGroupMessageDTO dto) {
        sendNotificationAsync(userIds, ImConversationTypeEnum.GROUP.getType(), dto.getType(), dto);
    }

    @Override
    public void sendChannelMessageAsync(Collection<Long> userIds, ImChannelMessageDTO dto) {
        sendNotificationAsync(userIds, ImConversationTypeEnum.CHANNEL.getType(), dto.getType(), dto);
    }

    @Override
    public void broadcastChannelMessageAsync(ImChannelMessageDTO dto) {
        broadcastNotificationAsync(ImConversationTypeEnum.CHANNEL.getType(), dto.getType(), dto);
    }

    /**
     * 异步发送 IM WebSocket 通知；多收件人共享同一通知对象，避免按收件人重复注册 afterCommit 回调
     */
    public void doSendNotification(Collection<Long> userIds, ImNotificationWebSocketBo notification) {
        for (Long userId : getDistinctUserIds(userIds)) {
            try {
                webSocketSenderApi.sendObject(UserTypeEnum.ADMIN.getValue(), userId,
                        ImNotificationWebSocketBo.TYPE, notification);
            } catch (Exception e) {
                log.error("[doSendNotification][userId({}) notification({}) 发送失败]", userId, notification, e);
            }
        }
    }

    /**
     * 异步广播 IM WebSocket 通知给当前所有在线管理端用户
     */
    public void doBroadcastNotification(ImNotificationWebSocketBo notification) {
        try {
            webSocketSenderApi.sendObject(UserTypeEnum.ADMIN.getValue(),
                    ImNotificationWebSocketBo.TYPE, notification);
        } catch (Exception e) {
            log.error("[doBroadcastNotification][notification({}) 广播失败]", notification, e);
        }
    }

    private static ImNotificationWebSocketBo buildNotification(Integer conversationType, Integer contentType, Object payload) {
        return new ImNotificationWebSocketBo()
                .setConversationType(conversationType)
                .setContentType(contentType)
                .setPayload(payload);
    }

    private static Set<Long> getDistinctUserIds(Collection<Long> userIds) {
        LinkedHashSet<Long> distinctUserIds = new LinkedHashSet<>();
        if (userIds == null) {
            return distinctUserIds;
        }
        userIds.stream().filter(Objects::nonNull).forEach(distinctUserIds::add);
        return distinctUserIds;
    }

    /**
     * 事务感知的任务调度：
     * - 有事务：注册 afterCommit 回调，事务提交后再执行，防止客户端拿到消息去查库时数据还没落盘
     * - 无事务：直接执行（如非 @Transactional 方法中的调用）
     *
     * @param task 待执行的推送任务
     */
    private void executeAfterTransaction(Runnable task) {
        Runnable asyncTask = () -> CompletableFuture.runAsync(task);
        // 情况一：没有事务，直接执行
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            asyncTask.run();
            return;
        }
        // 情况二：有事务，注册 afterCommit 事件，在事务提交后执行
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                asyncTask.run();
            }

        });
    }

}
