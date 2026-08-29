package com.fly.ai.demo.http.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * AI 模型 HTTP 调用日志测试。
 *
 * @author lxs
 * @date 2026-08-26
 */
class AiHttpLogTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 校验 Chat Completions 响应仅截断模型回答，其余元数据完整保留。
     *
     * @throws Exception JSON 解析失败时抛出
     */
    @Test
    void shouldOnlyTruncateChatContent() throws Exception {
        String content = "a".repeat(60);
        String response = """
                {"model":"qwen-plus","id":"chatcmpl-test","created":123,"choices":[{"index":0,"message":{"role":"assistant","content":"%s"},"finish_reason":"stop"}],"usage":{"prompt_tokens":10,"completion_tokens":20,"total_tokens":30}}
                """.formatted(content);

        JsonNode loggedResponse = objectMapper.readTree(AiHttpLog.formatResponse(objectMapper, response));

        assertEquals("qwen-plus", loggedResponse.path("model").asText());
        assertEquals("chatcmpl-test", loggedResponse.path("id").asText());
        assertEquals(123, loggedResponse.path("created").asInt());
        assertEquals("stop", loggedResponse.path("choices").path(0).path("finish_reason").asText());
        assertEquals("a".repeat(50) + "...", loggedResponse.path("choices").path(0).path("message").path("content").asText());
    }

    /**
     * 校验流式响应在所有事件范围内仅保留前 50 个模型输出字符，且不丢失事件元数据。
     *
     * @throws Exception JSON 解析失败时抛出
     */
    @Test
    void shouldOnlyRetainFirstFiftyCharactersAcrossStreamEvents() throws Exception {
        String firstEvent = """
                {"id":"chatcmpl-test","choices":[{"index":0,"delta":{"content":"%s"},"finish_reason":null}]}
                """.formatted("a".repeat(30));
        String secondEvent = """
                {"id":"chatcmpl-test","choices":[{"index":0,"delta":{"content":"%s"},"finish_reason":"stop"}],"usage":{"total_tokens":60}}
                """.formatted("b".repeat(30));

        JsonNode loggedEvents = objectMapper.readTree(AiHttpLog.formatStreamResponse(objectMapper, List.of(firstEvent, secondEvent)));

        assertEquals("a".repeat(30), loggedEvents.path(0).path("choices").path(0).path("delta").path("content").asText());
        assertEquals("b".repeat(20), loggedEvents.path(1).path("choices").path(0).path("delta").path("content").asText());
        assertEquals("stop", loggedEvents.path(1).path("choices").path(0).path("finish_reason").asText());
        assertEquals(60, loggedEvents.path(1).path("usage").path("total_tokens").asInt());
    }
}
