package com.fly.im.service.websocket;

import cn.hutool.extra.spring.SpringUtil;
import com.fly.common.enums.user.UserTypeEnum;
import com.fly.im.service.websocket.dto.ImChannelMessageDTO;
import com.fly.im.service.websocket.dto.ImGroupMessageDTO;
import com.fly.im.service.websocket.dto.ImPrivateMessageDTO;
import com.fly.im.framework.websocket.WebSocketSenderApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
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
    public void sendPrivateMessageAsync(Collection<Long> userIds, ImPrivateMessageDTO dto) {
        // 说明：通过 executeAfterTransaction 保证事务提交后再推送，避免客户端收到消息后查询数据库时事务尚未提交
        // 通过 getSelf() 获取 Spring 代理对象调用 @Async 方法，确保异步 AOP 生效（直接 this 调用会绕过代理）
        executeAfterTransaction(() -> getSelf().doSendPrivateMessage(userIds, dto));
    }

    @Override
    public void sendGroupMessageAsync(Collection<Long> userIds, ImGroupMessageDTO dto) {
        executeAfterTransaction(() -> getSelf().doSendGroupMessage(userIds, dto));
    }

    @Override
    public void sendChannelMessageAsync(Collection<Long> userIds, ImChannelMessageDTO dto) {
        executeAfterTransaction(() -> getSelf().doSendChannelMessage(userIds, dto));
    }

    @Override
    public void broadcastChannelMessageAsync(ImChannelMessageDTO dto) {
        executeAfterTransaction(() -> getSelf().doBroadcastChannelMessage(dto));
    }

    /**
     * 异步发送私聊 WebSocket 消息；多收件人共享同一 dto，避免按收件人重复注册 afterCommit 回调
     */
    @Async
    public void doSendPrivateMessage(Collection<Long> userIds, ImPrivateMessageDTO dto) {
        for (Long userId : getDistinctUserIds(userIds)) {
            try {
                webSocketSenderApi.sendObject(UserTypeEnum.ADMIN.getValue(), userId,
                        ImPrivateMessageDTO.TYPE, dto);
            } catch (Exception e) {
                log.error("[doSendPrivateMessage][userId({}) dto({}) 发送失败]", userId, dto, e);
            }
        }
    }

    /**
     * 异步发送群聊 WebSocket 消息
     */
    @Async
    public void doSendGroupMessage(Collection<Long> userIds, ImGroupMessageDTO dto) {
        for (Long userId : getDistinctUserIds(userIds)) {
            try {
                webSocketSenderApi.sendObject(UserTypeEnum.ADMIN.getValue(), userId,
                        ImGroupMessageDTO.TYPE, dto);
            } catch (Exception e) {
                log.error("[doSendGroupMessage][userId({}) dto({}) 发送失败]", userId, dto, e);
            }
        }
    }

    /**
     * 异步发送频道 WebSocket 消息
     */
    @Async
    public void doSendChannelMessage(Collection<Long> userIds, ImChannelMessageDTO dto) {
        for (Long userId : getDistinctUserIds(userIds)) {
            try {
                webSocketSenderApi.sendObject(UserTypeEnum.ADMIN.getValue(), userId,
                        ImChannelMessageDTO.TYPE, dto);
            } catch (Exception e) {
                log.error("[doSendChannelMessage][userId({}) dto({}) 发送失败]", userId, dto, e);
            }
        }
    }

    /**
     * 异步广播频道 WebSocket 消息给当前所有在线管理端用户；
     * 依赖 infra WebSocketSenderApi 按 UserType 广播能力，离线用户由客户端上线 pull 兜底
     */
    @Async
    public void doBroadcastChannelMessage(ImChannelMessageDTO dto) {
        try {
            webSocketSenderApi.sendObject(UserTypeEnum.ADMIN.getValue(),
                    ImChannelMessageDTO.TYPE, dto);
        } catch (Exception e) {
            log.error("[doBroadcastChannelMessage][dto({}) 广播失败]", dto, e);
        }
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
     * @param task 待执行的推送任务（内部通过 getSelf() 走 @Async 异步执行）
     */
    private void executeAfterTransaction(Runnable task) {
        // 情况一：没有事务，直接执行
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            task.run();
            return;
        }
        // 情况二：有事务，注册 afterCommit 事件，在事务提交后执行
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                task.run();
            }

        });
    }

    /**
     * 获得自身的代理对象，解决 @Async AOP 代理问题
     */
    private ImWebSocketServiceImpl getSelf() {
//        return SpringUtil.getBean(getClass());
        return SpringUtil.getBean(ImWebSocketServiceImpl.class);
    }

}
