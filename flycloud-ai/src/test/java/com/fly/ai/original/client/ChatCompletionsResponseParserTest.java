package com.fly.ai.original.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.ai.original.client.tool.ChatCompletionsResponseParser;
import com.fly.ai.common.model.AiChatResponse;
import com.fly.ai.common.model.AiStreamEvent;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 验证 DeepSeek、百炼共用的 Chat Completions 协议解析。
 */
class ChatCompletionsResponseParserTest {

    private final ChatCompletionsResponseParser parser = new ChatCompletionsResponseParser(new ObjectMapper());

    @Test
    void shouldParseChatCompletionAndStreamDelta() {
        AiChatResponse response = parser.parseChatResponse("""
                {
                  "id": "chatcmpl_123",
                  "model": "qwen-plus",
                  "choices": [{"message": {"role": "assistant", "content": "你好"}}],
                  "usage": {"prompt_tokens": 10, "completion_tokens": 5, "total_tokens": 15}
                }
                """, "fallback-model");
        Optional<AiStreamEvent> event = parser.parseStreamEvent("""
                {"id": "chatcmpl_123", "choices": [{"delta": {"content": "你"}}]}
                """);

        assertEquals("chatcmpl_123", response.responseId());
        assertEquals("qwen-plus", response.model());
        assertEquals("你好", response.content());
        assertEquals(15, response.usage().totalTokens());
        assertTrue(event.isPresent());
        assertEquals("你", event.get().delta());
    }

}
