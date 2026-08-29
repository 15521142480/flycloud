package com.fly.ai.chat.memory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.ai.common.config.AiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * 基于 Redis 的 Spring AI {@link ChatMemoryRepository}。
 * <p>
 * Redis 只保存传给模型的短期 user/assistant/system 上下文；完整消息历史由 MySQL 的 {@code ai_message}
 * 表负责，二者职责不混用。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiRedisChatMemoryRepository implements ChatMemoryRepository {

    private static final String KEY_PREFIX = "flycloud:ai:chat-memory:";

    private final StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper;

    private final AiProperties aiProperties;

    /**
     * 获取会话最近上下文。
     *
     * @param conversationId 会话标识
     * @return Spring AI 消息列表
     */
    @Override
    public List<Message> findByConversationId(String conversationId) {
        String json = stringRedisTemplate.opsForValue().get(key(conversationId));
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            List<MemoryMessage> messages = objectMapper.readValue(json, new TypeReference<>() {
            });
            return messages.stream().map(this::toSpringAiMessage).toList();
        } catch (Exception exception) {
            log.warn("AI ChatMemory Redis 数据无法解析，conversationId={}", conversationId, exception);
            deleteByConversationId(conversationId);
            return Collections.emptyList();
        }
    }

    /**
     * 保存被 Spring AI 消息窗口裁剪后的会话上下文，并刷新 TTL。
     *
     * @param conversationId 会话标识
     * @param messages 消息窗口中的上下文消息
     */
    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        try {
            List<MemoryMessage> memoryMessages = messages.stream()
                    .filter(message -> message instanceof UserMessage || message instanceof AssistantMessage
                            || message instanceof SystemMessage)
                    .map(MemoryMessage::from)
                    .toList();
            Duration ttl = aiProperties.getMemory().getRedisTtl();
            stringRedisTemplate.opsForValue().set(key(conversationId), objectMapper.writeValueAsString(memoryMessages), ttl);
        } catch (Exception exception) {
            throw new IllegalStateException("保存 AI ChatMemory Redis 上下文失败", exception);
        }
    }

    /**
     * 删除指定会话的短期上下文。
     *
     * @param conversationId 会话标识
     */
    @Override
    public void deleteByConversationId(String conversationId) {
        stringRedisTemplate.delete(key(conversationId));
    }

    /**
     * 会话列表由 MySQL 的正式会话表维护，Redis 仓库不承担枚举会话的职责。
     *
     * @return 空列表
     */
    @Override
    public List<String> findConversationIds() {
        return Collections.emptyList();
    }

    /**
     * 构建 Redis Key。
     *
     * @param conversationId 会话标识
     * @return Redis Key
     */
    private String key(String conversationId) {
        return KEY_PREFIX + conversationId;
    }

    /**
     * 将 Redis DTO 转换回 Spring AI 消息。
     *
     * @param message Redis 消息 DTO
     * @return Spring AI 消息
     */
    private Message toSpringAiMessage(MemoryMessage message) {
        return switch (message.role()) {
            case "assistant" -> new AssistantMessage(message.content());
            case "system" -> new SystemMessage(message.content());
            default -> new UserMessage(message.content());
        };
    }

    /**
     * Redis 中的最小短期上下文结构；不保存数据库审计字段或工具细节。
     *
     * @param role 消息角色
     * @param content 消息内容
     */
    record MemoryMessage(String role, String content) {

        /**
         * 从 Spring AI 消息构建 Redis DTO。
         *
         * @param message Spring AI 消息
         * @return Redis DTO
         */
        private static MemoryMessage from(Message message) {
            return new MemoryMessage(message.getMessageType().getValue(), message.getText());
        }
    }
}
