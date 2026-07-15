package com.fly.common.rocketmq.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.common.rocketmq.domain.MqMessage;
import com.fly.common.rocketmq.domain.MqOutboxMessage;
import com.fly.common.rocketmq.mapper.MqOutboxMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 本地消息表服务。
 *
 * <p>业务服务必须在更新业务数据的同一事务中调用 {@link #save}；发送由调度器在事务提交后异步完成。</p>
 */
@RequiredArgsConstructor
public class MqOutboxService {

    /** 待投递状态。 */
    public static final int STATUS_PENDING = 0;
    /** 已被 Broker 确认状态。 */
    public static final int STATUS_SENT = 1;
    /** 达到最大重试次数后的最终失败状态。 */
    public static final int STATUS_FAILED = 2;
    /** 已被某个调度实例持有租约的投递中状态。 */
    public static final int STATUS_SENDING = 3;

    private final MqOutboxMessageMapper mapper;
    private final ObjectMapper objectMapper;

    /**
     * 在业务事务中创建标准消息信封与本地消息表记录。
     *
     * @return 包含 messageId、occurredTime、bizKey 的消息信封
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessage<?> save(String topic, String tag, String bizKey, String eventType, Object payload) {
        MqMessage<Object> message = new MqMessage<>();
        message.setMessageId(UUID.randomUUID().toString().replace("-", ""));
        message.setOccurredTime(LocalDateTime.now());
        message.setBizKey(bizKey);
        message.setEventType(eventType);
        message.setPayload(payload);
        MqOutboxMessage outbox = new MqOutboxMessage();
        outbox.setMessageId(message.getMessageId());
        outbox.setTopic(topic);
        outbox.setTag(tag);
        outbox.setBizKey(bizKey);
        outbox.setPayload(writeValue(message));
        outbox.setStatus(STATUS_PENDING);
        outbox.setRetryCount(0);
        outbox.setNextRetryTime(LocalDateTime.now());
        outbox.setCreateTime(LocalDateTime.now());
        outbox.setUpdateTime(LocalDateTime.now());
        mapper.insert(outbox);
        return message;
    }

    /**
     * 查询本轮可抢占的本地消息。
     *
     * <p>结果仍需调用 {@link #claim(Long, long)} 进行条件抢占，查询本身不提供并发互斥。</p>
     */
    public List<MqOutboxMessage> findPending(int batchSize) {
        return mapper.selectPendingMessages(batchSize);
    }

    /**
     * 以条件更新抢占待发送消息，避免多实例重复调度同一条记录。
     *
     * @param messageId 本地消息表主键
     * @param claimLeaseSeconds 抢占租约，实例异常退出后可由其他实例重新投递
     * @return 抢占令牌；未抢到时返回 {@code null}
     */
    public String claim(Long messageId, long claimLeaseSeconds) {
        LocalDateTime now = LocalDateTime.now();
        String dispatchToken = UUID.randomUUID().toString().replace("-", "");
        LambdaUpdateWrapper<MqOutboxMessage> wrapper = new LambdaUpdateWrapper<MqOutboxMessage>()
                .eq(MqOutboxMessage::getId, messageId)
                .in(MqOutboxMessage::getStatus, STATUS_PENDING, STATUS_SENDING)
                .le(MqOutboxMessage::getNextRetryTime, now)
                .set(MqOutboxMessage::getStatus, STATUS_SENDING)
                .set(MqOutboxMessage::getDispatchToken, dispatchToken)
                .set(MqOutboxMessage::getNextRetryTime, now.plusSeconds(claimLeaseSeconds))
                .set(MqOutboxMessage::getUpdateTime, now);
        return mapper.update(null, wrapper) > 0 ? dispatchToken : null;
    }

    /** 将本地消息 JSON 反序列化为统一信封。 */
    public MqMessage<?> readMessage(MqOutboxMessage outbox) {
        try {
            return objectMapper.readValue(outbox.getPayload(), MqMessage.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("本地消息表 payload 反序列化失败，messageId=" + outbox.getMessageId(), exception);
        }
    }

    /** 标记当前抢占令牌对应的消息已被 Broker 成功确认。 */
    public void markSent(Long id, String dispatchToken) {
        updateClaimed(id, dispatchToken, STATUS_SENT, null, null, null);
    }

    /** 记录当前抢占令牌对应的异步投递失败，并按指数退避安排下次重试。 */
    public void markFailure(MqOutboxMessage outbox, String dispatchToken, int maxRetryCount, Throwable throwable) {
        int retryCount = outbox.getRetryCount() + 1;
        int status = retryCount >= maxRetryCount ? STATUS_FAILED : STATUS_PENDING;
        long delaySeconds = Math.min(300, 1L << Math.min(retryCount, 8));
        updateClaimed(outbox.getId(), dispatchToken, status, retryCount, LocalDateTime.now().plusSeconds(delaySeconds),
                throwable == null ? "未知发送异常" : truncate(throwable.getMessage()));
    }

    /** 使用抢占令牌更新本地消息投递状态，防止租约过期后的旧实例覆盖新实例结果。 */
    private void updateClaimed(Long id, String dispatchToken, int status, Integer retryCount,
                               LocalDateTime nextRetryTime, String lastError) {
        LambdaUpdateWrapper<MqOutboxMessage> wrapper = new LambdaUpdateWrapper<MqOutboxMessage>()
                .eq(MqOutboxMessage::getId, id)
                .eq(MqOutboxMessage::getStatus, STATUS_SENDING)
                .eq(MqOutboxMessage::getDispatchToken, dispatchToken)
                .set(MqOutboxMessage::getStatus, status)
                .set(MqOutboxMessage::getDispatchToken, null)
                .set(MqOutboxMessage::getUpdateTime, LocalDateTime.now());
        if (retryCount != null) {
            wrapper.set(MqOutboxMessage::getRetryCount, retryCount);
        }
        if (nextRetryTime != null) {
            wrapper.set(MqOutboxMessage::getNextRetryTime, nextRetryTime);
        }
        if (lastError != null) {
            wrapper.set(MqOutboxMessage::getLastError, lastError);
        }
        mapper.update(null, wrapper);
    }

    /** 序列化统一消息信封。 */
    private String writeValue(MqMessage<?> message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("本地消息表 payload 序列化失败", exception);
        }
    }

    /** 截断异常文本，避免超过表字段上限。 */
    private String truncate(String value) {
        if (value == null) {
            return null;
        }
        return value.length() <= 1000 ? value : value.substring(0, 1000);
    }
}
