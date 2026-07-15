package com.fly.common.rocketmq.consumer;

import com.fly.common.rocketmq.idempotent.domain.MqMessage;
import com.fly.common.rocketmq.idempotent.service.MqConsumeIdempotentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQListener;

/** 统一消费者抽象：幂等、异常日志、RocketMQ 框架重试。 */
@Slf4j
public abstract class AbstractIdempotentRocketMqConsumer<T> implements RocketMQListener<String> {

    private final MqConsumeIdempotentService idempotentService;
    private final ObjectMapper objectMapper;
    private final Class<T> payloadType;

    /**
     * 创建统一消费者。
     *
     * @param idempotentService 消费幂等服务
     * @param objectMapper 消息 JSON 转换器
     * @param payloadType 业务消息载荷类型
     */
    protected AbstractIdempotentRocketMqConsumer(MqConsumeIdempotentService idempotentService, ObjectMapper objectMapper,
                                                 Class<T> payloadType) {
        this.idempotentService = idempotentService;
        this.objectMapper = objectMapper;
        this.payloadType = payloadType;
    }

    /**
     * 处理 RocketMQ 原始消息；幂等记录先参与本地事务，处理异常直接抛出以触发框架重试。
     *
     * @param rawMessage RocketMQ 传入的 JSON 文本
     */
    @Override
    public final void onMessage(String rawMessage) {
        MqMessage<T> message = deserialize(rawMessage);
        try {
            boolean consumed = idempotentService.consume(consumerGroup(), topic(), tag(), message, () -> handle(message));
            if (!consumed) {
                log.info("重复 RocketMQ 消息已忽略，consumerGroup={}, messageId={}", consumerGroup(), message.getMessageId());
            }
        } catch (Exception exception) {
            log.error("RocketMQ 消费失败，将由框架重试，consumerGroup={}, messageId={}", consumerGroup(), message.getMessageId(), exception);
            throw exception;
        }
    }

    /** @return 消费者组常量。 */
    protected abstract String consumerGroup();

    /** @return 当前消费者监听的 Topic 常量。 */
    protected abstract String topic();

    /** @return 当前消费者监听的 Tag 常量。 */
    protected abstract String tag();

    /** 执行业务处理；异常必须继续抛出以便 RocketMQ 重试。 */
    protected abstract void handle(MqMessage<T> message);

    /** 将统一消息信封反序列化为强类型业务载荷。 */
    private MqMessage<T> deserialize(String rawMessage) {
        try {
            MqMessage<?> envelope = objectMapper.readValue(rawMessage, MqMessage.class);
            MqMessage<T> message = new MqMessage<>();
            message.setMessageId(envelope.getMessageId());
            message.setOccurredTime(envelope.getOccurredTime());
            message.setBizKey(envelope.getBizKey());
            message.setEventType(envelope.getEventType());
            message.setPayload(objectMapper.convertValue(envelope.getPayload(), payloadType));
            return message;
        } catch (Exception exception) {
            throw new IllegalArgumentException("RocketMQ 消息反序列化失败", exception);
        }
    }
}
