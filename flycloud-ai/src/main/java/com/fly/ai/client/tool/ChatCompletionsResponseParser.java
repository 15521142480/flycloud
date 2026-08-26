package com.fly.ai.client.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.ai.model.AiChatResponse;
import com.fly.ai.model.AiStreamEvent;
import com.fly.ai.model.AiUsage;
import com.fly.ai.model.AiEmbeddingResponse;
import com.fly.common.exception.AiProviderException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Chat Completions 协议响应解析器。
 * <p>
 * DeepSeek、阿里云百炼均采用该协议；该类只表达协议，不代表任何模型供应商。
 *
 * @author lxs
 * @date 2026-08-25
 */
public class ChatCompletionsResponseParser {

    private final ObjectMapper objectMapper;

    /**
     * 创建协议响应解析器。
     *
     * @param objectMapper JSON 解析工具
     */
    public ChatCompletionsResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 解析普通聊天响应。
     *
     * @param responseBody 原始响应 JSON
     * @param defaultModel 默认模型名称
     * @return 统一聊天响应
     */
    public AiChatResponse parseChatResponse(String responseBody, String defaultModel) {
        JsonNode root = readTree(responseBody);
        JsonNode choice = root.path("choices").path(0);
        return new AiChatResponse(root.path("id").asText(), root.path("model").asText(defaultModel),
                choice.path("message").path("content").asText(), parseUsage(root.path("usage")));
    }

    /**
     * 解析文本向量化响应。
     *
     * @param responseBody 原始响应 JSON
     * @param defaultModel 默认模型名称
     * @return 统一向量响应
     */
    public AiEmbeddingResponse parseEmbeddingResponse(String responseBody, String defaultModel) {
        JsonNode root = readTree(responseBody);
        List<Float> embedding = new ArrayList<>();
        for (JsonNode value : root.path("data").path(0).path("embedding")) {
            embedding.add(value.floatValue());
        }
        JsonNode usage = root.path("usage");
        return new AiEmbeddingResponse(root.path("model").asText(defaultModel), embedding,
                new AiEmbeddingResponse.EmbeddingUsage(usage.path("prompt_tokens").asLong(), usage.path("total_tokens").asLong()));
    }

    /**
     * 解析单个 Chat Completions SSE 数据片段。
     *
     * @param data SSE 的 data 内容
     * @return 可识别时返回统一流式事件，否则为空
     */
    public Optional<AiStreamEvent> parseStreamEvent(String data) {
        JsonNode root = readTree(data);
        JsonNode usage = root.path("usage");
        if (!usage.isMissingNode() && !usage.isNull()) {
            return Optional.of(AiStreamEvent.completed(root.path("id").asText(), parseUsage(usage)));
        }
        String delta = root.path("choices").path(0).path("delta").path("content").asText();
        return delta.isBlank() ? Optional.empty() : Optional.of(AiStreamEvent.delta(delta));
    }

    /**
     * 从上游错误响应中提取可展示的错误信息。
     *
     * @param responseBody 原始错误响应 JSON
     * @return 错误说明
     */
    public String parseErrorMessage(String responseBody) {
        try {
            JsonNode root = readTree(responseBody);
            String message = root.path("error").path("message").asText();
            if (message.isBlank()) {
                message = root.path("message").asText();
            }
            return message.isBlank() ? "模型服务调用失败" : message;
        } catch (AiProviderException ignored) {
            return "模型服务返回了无法解析的错误响应";
        }
    }

    /**
     * 解析 Token 用量字段。
     *
     * @param usage 用量 JSON 节点
     * @return 统一 Token 用量
     */
    private AiUsage parseUsage(JsonNode usage) {
        return new AiUsage(usage.path("prompt_tokens").asLong(), usage.path("completion_tokens").asLong(),
                usage.path("total_tokens").asLong());
    }

    /**
     * 将 JSON 字符串解析为树结构。
     *
     * @param body 原始 JSON 字符串
     * @return JSON 根节点
     */
    private JsonNode readTree(String body) {
        try {
            return objectMapper.readTree(body);
        } catch (JsonProcessingException exception) {
            throw new AiProviderException(502, "模型服务返回了无法解析的响应", exception);
        }
    }

}
