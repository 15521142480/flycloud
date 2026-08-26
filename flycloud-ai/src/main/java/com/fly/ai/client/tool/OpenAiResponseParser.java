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
 * OpenAI 原始 JSON 响应解析器。
 * <p>
 * 将请求与响应 JSON 的解析单独放在此处，便于学习接口字段和后续适配其他供应商。
 *
 * @author lxs
 * @date 2026-08-25
 */
public class OpenAiResponseParser {

    private final ObjectMapper objectMapper;

    public OpenAiResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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

    public String parseErrorMessage(String responseBody) {
        try {
            return errorMessage(readTree(responseBody));
        } catch (AiProviderException ignored) {
            return "模型服务返回了无法解析的错误响应";
        }
    }

    private AiUsage parseUsage(JsonNode usage) {
        return new AiUsage(usage.path("input_tokens").asLong(), usage.path("output_tokens").asLong(),
                usage.path("total_tokens").asLong());
    }

    private String errorMessage(JsonNode root) {
        String message = root.path("error").path("message").asText();
        if (message.isBlank()) {
            message = root.path("message").asText();
        }
        return message.isBlank() ? "模型服务调用失败" : message;
    }

    private JsonNode readTree(String body) {
        try {
            return objectMapper.readTree(body);
        } catch (JsonProcessingException exception) {
            throw new AiProviderException(502, "模型服务返回了无法解析的响应", exception);
        }
    }

}
