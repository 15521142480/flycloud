package com.fly.common.rocketmq.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fly.common.rocketmq.idempotent.domain.MqMessage;
import com.fly.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * RocketMQ 统一消息信封编解码器。
 *
 * <p>本地消息表、生产者和消费者必须通过本类使用同一个项目级 {@link ObjectMapper}，确保消息在持久化、
 * 投递和消费三个阶段遵循相同的 JSON 数据契约。</p>
 */
@RequiredArgsConstructor
public class MqMessageCodec {

    private final ObjectMapper objectMapper;

    /**
     * 使用项目统一 JSON 规则序列化消息信封。
     *
     * @param message 统一消息信封
     * @return JSON 消息文本
     */
    public String serialize(MqMessage<?> message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("RocketMQ 消息序列化失败，messageId=" + message.getMessageId(), exception);
        }
    }

    /**
     * 按项目统一 JSON 规则反序列化消息信封。
     *
     * <p>兼容首次集成期间由 RocketMQ Starter 默认转换器生成的 ISO-8601 时间。该兼容仅用于处理
     * 已进入 Broker 的历史消息；修复后的生产者始终只生成项目约定的 {@code yyyy-MM-dd HH:mm:ss}。</p>
     *
     * @param rawMessage RocketMQ 原始 JSON 文本
     * @return 统一消息信封
     */
    public MqMessage<?> deserialize(String rawMessage) {
        try {
            return objectMapper.readValue(rawMessage, MqMessage.class);
        } catch (JsonProcessingException exception) {
            return deserializeLegacyIsoTime(rawMessage, exception);
        }
    }

    /**
     * 将旧消息中的 ISO-8601 发生时间规范化为项目统一格式后再解析。
     */
    private MqMessage<?> deserializeLegacyIsoTime(String rawMessage, JsonProcessingException originalException) {
        try {
            JsonNode root = objectMapper.readTree(rawMessage);
            if (!(root instanceof ObjectNode objectNode)) {
                throw originalException;
            }
            JsonNode occurredTime = objectNode.get("occurredTime");
            if (occurredTime == null || !occurredTime.isTextual() || !occurredTime.asText().contains("T")) {
                throw originalException;
            }
            LocalDateTime time = LocalDateTime.parse(occurredTime.asText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            objectNode.put("occurredTime", DateUtils.DATETIMEFORMATTER.format(time));
            return objectMapper.treeToValue(objectNode, MqMessage.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException("RocketMQ 消息反序列化失败：" + originalException.getOriginalMessage(),
                    originalException);
        }
    }

    /**
     * 将消息载荷转换为消费者声明的强类型对象。
     *
     * @param payload 原始消息载荷
     * @param payloadType 消费者期望的载荷类型
     * @param <T> 载荷类型
     * @return 强类型业务载荷
     */
    public <T> T convertPayload(Object payload, Class<T> payloadType) {
        return objectMapper.convertValue(payload, payloadType);
    }
}
