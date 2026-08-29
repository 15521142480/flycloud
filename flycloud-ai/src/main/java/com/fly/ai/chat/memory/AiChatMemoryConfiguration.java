package com.fly.ai.chat.memory;

import com.fly.ai.common.config.AiProperties;
import com.fly.common.exception.AiProviderException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 统一聊天短期记忆配置。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Configuration
@RequiredArgsConstructor
public class AiChatMemoryConfiguration {

    private final AiProperties aiProperties;

    /**
     * 创建 Spring AI 消息窗口记忆。
     *
     * @param repository Redis 聊天记忆仓库
     * @return 按 conversationId 隔离的 ChatMemory
     */
    @Bean
    public ChatMemory aiChatMemory(AiRedisChatMemoryRepository repository) {
        if (aiProperties.getMemory().getMaxMessages() <= 0) {
            throw new AiProviderException(500, "flycloud.ai.memory.max-messages 必须大于 0");
        }
        if (aiProperties.getMemory().getRedisTtl() == null || aiProperties.getMemory().getRedisTtl().isNegative()
                || aiProperties.getMemory().getRedisTtl().isZero()) {
            throw new AiProviderException(500, "flycloud.ai.memory.redis-ttl 必须大于 0");
        }
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(aiProperties.getMemory().getMaxMessages())
                .build();
    }
}
