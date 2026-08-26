package com.fly.ai.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.ai.client.tool.OpenAiResponseParser;
import com.fly.ai.model.AiChatResponse;
import com.fly.ai.model.AiStreamEvent;
import com.fly.ai.model.AiEmbeddingResponse;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 原生供应商 JSON 解析测试，不发起真实模型请求。
 */
class OpenAiResponseParserTest {

    private final OpenAiResponseParser parser = new OpenAiResponseParser(new ObjectMapper());

    @Test
    void shouldParseResponsesApiChatResponse() {
        String response = """
                {
                  "id": "resp_123",
                  "model": "gpt-test",
                  "output": [{
                    "type": "message",
                    "content": [{"type": "output_text", "text": "你好"}]
                  }],
                  "usage": {"input_tokens": 10, "output_tokens": 5, "total_tokens": 15}
                }
                """;

        AiChatResponse result = parser.parseChatResponse(response, "fallback-model");

        assertEquals("resp_123", result.responseId());
        assertEquals("gpt-test", result.model());
        assertEquals("你好", result.content());
        assertEquals(15, result.usage().totalTokens());
    }

    @Test
    void shouldParseEmbeddingAndStreamEvents() {
        AiEmbeddingResponse embedding = parser.parseEmbeddingResponse("""
                {
                  "model": "text-embedding-test",
                  "data": [{"embedding": [0.1, -0.2]}],
                  "usage": {"prompt_tokens": 3, "total_tokens": 3}
                }
                """, "fallback-model");
        Optional<AiStreamEvent> event = parser.parseStreamEvent("""
                {"type": "response.output_text.delta", "delta": "你"}
                """);

        assertEquals("text-embedding-test", embedding.model());
        assertEquals(2, embedding.embedding().size());
        assertTrue(event.isPresent());
        assertEquals("你", event.get().delta());
    }

}
