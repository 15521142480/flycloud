package com.fly.ai.original.client.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.ai.common.model.AiChatResponse;
import com.fly.ai.common.model.AiStreamEvent;
import com.fly.ai.common.model.AiUsage;
import com.fly.ai.common.model.AiEmbeddingResponse;
import com.fly.common.exception.AiProviderException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * OpenAI 原始 JSON 响应解析器。
 * <p>
 * 将请求与响应 JSON 的解析单独放在此处，便于学习接口字段和后续适配其他供应商。
 *
 * @author lxs
 * @date 2026-08-25
 */
public class OpenAiResponseParser {

    private final ObjectMapper objectMapper;

    /**
     * 创建 OpenAI 响应解析器。
     *
     * @param objectMapper JSON 解析工具
     */
    public OpenAiResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 解析 OpenAI Responses API 的普通聊天响应。
     *
     * @param responseBody 原始响应 JSON
     * @param defaultModel 默认模型名称
     * @return 统一聊天响应
     */
    public AiChatResponse parseChatResponse(String responseBody, String defaultModel) {
        JsonNode root = readTree(responseBody);
        StringBuilder content = new StringBuilder();
        for (JsonNode output : root.path("output")) {
            if (!"message".equals(output.path("type").asText())) {
                continue;
            }
            for (JsonNode item : output.path("content")) {
                if ("output_text".equals(item.path("type").asText())) {
                    content.append(item.path("text").asText());
                }
            }
        }
        if (content.isEmpty()) {
            content.append(root.path("output_text").asText());
        }
        return new AiChatResponse(root.path("id").asText(), root.path("model").asText(defaultModel), content.toString(),
                parseUsage(root.path("usage")));
    }

    /**
     * 解析 OpenAI Embeddings API 的响应。
     *
     * @param responseBody 原始响应 JSON
     * @param defaultModel 默认模型名称
     * @return 统一向量响应
     */
    public AiEmbeddingResponse parseEmbeddingResponse(String responseBody, String defaultModel) {
        JsonNode root = readTree(responseBody);
        JsonNode embeddingNode = root.path("data").path(0).path("embedding");
        List<Float> embedding = new ArrayList<>();
        for (JsonNode value : embeddingNode) {
            embedding.add(value.floatValue());
        }
        JsonNode usage = root.path("usage");
        return new AiEmbeddingResponse(root.path("model").asText(defaultModel), embedding,
                new AiEmbeddingResponse.EmbeddingUsage(usage.path("prompt_tokens").asLong(), usage.path("total_tokens").asLong()));
    }

    /**
     * 解析单个 OpenAI SSE 数据片段。
     *
     * @param data SSE 的 data 内容
     * @return 可识别时返回统一流式事件，否则为空
     */
    public Optional<AiStreamEvent> parseStreamEvent(String data) {
        JsonNode root = readTree(data);
        String type = root.path("type").asText();
        if ("response.output_text.delta".equals(type)) {
            return Optional.of(AiStreamEvent.delta(root.path("delta").asText()));
        }
        if ("response.completed".equals(type)) {
            JsonNode response = root.path("response");
            return Optional.of(AiStreamEvent.completed(response.path("id").asText(), parseUsage(response.path("usage"))));
        }
        if ("error".equals(type)) {
            return Optional.of(AiStreamEvent.error(errorMessage(root)));
        }
        return Optional.empty();
    }

    /**
     * 从 OpenAI 错误响应中提取可展示的错误信息。
     *
     * @param responseBody 原始错误响应 JSON
     * @return 错误说明
     */
    public String parseErrorMessage(String responseBody) {
        try {
            return errorMessage(readTree(responseBody));
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
        return new AiUsage(usage.path("input_tokens").asLong(), usage.path("output_tokens").asLong(),
                usage.path("total_tokens").asLong());
    }

    /**
     * 从 OpenAI 响应根节点提取错误说明。
     *
     * @param root 响应根节点
     * @return 错误说明
     */
    private String errorMessage(JsonNode root) {
        String message = root.path("error").path("message").asText();
        if (message.isBlank()) {
            message = root.path("message").asText();
        }
        return message.isBlank() ? "模型服务调用失败" : message;
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
