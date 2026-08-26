package com.fly.ai.original.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpRequest;
import java.util.List;

/**
 * AI 模型 HTTP 调用日志。
 * <p>
 * 仅记录请求方法、地址和请求体，绝不记录 Authorization 等认证头。聊天响应会保留原始 JSON 的元数据，
 * 仅截取模型文本输出的前 50 个字符。
 *
 * @author lxs
 * @date 2026-08-26
 */
@Slf4j
final class AiHttpLog {

    private static final int MODEL_TEXT_LENGTH = 50;

    /**
     * 禁止创建工具类实例。
     */
    private AiHttpLog() {
    }

    /**
     * 记录模型 HTTP 请求，不记录认证头。
     *
     * @param providerName 供应商名称
     * @param request HTTP 请求
     * @param requestBody 请求 JSON
     */
    static void request(String providerName, HttpRequest request, String requestBody) {
        log.info("AI 模型请求，provider={}, method={}, url={}, body={}", providerName, request.method(), request.uri(), requestBody);
    }

    /**
     * 记录模型 HTTP 响应，仅截取模型文本输出的前 50 个字符。
     *
     * @param objectMapper JSON 序列化工具
     * @param providerName 供应商名称
     * @param request HTTP 请求
     * @param statusCode HTTP 状态码
     * @param responseBody 响应内容
     */
    static void response(ObjectMapper objectMapper, String providerName, HttpRequest request, int statusCode, String responseBody) {
        log.info("AI 模型响应，provider={}, method={}, url={}, statusCode={}, body={}",
                providerName, request.method(), request.uri(), statusCode, formatResponse(objectMapper, responseBody));
    }

    /**
     * 记录模型流式响应：保留每个 SSE 事件的元数据，所有事件合计仅保留前 50 个模型输出字符。
     *
     * @param objectMapper JSON 序列化工具
     * @param providerName 供应商名称
     * @param request HTTP 请求
     * @param statusCode HTTP 状态码
     * @param responseEvents SSE 的 data 内容集合
     */
    static void streamResponse(ObjectMapper objectMapper, String providerName, HttpRequest request, int statusCode,
            List<String> responseEvents) {
        log.info("AI 模型流式响应，provider={}, method={}, url={}, statusCode={}, body={}",
                providerName, request.method(), request.uri(), statusCode, formatStreamResponse(objectMapper, responseEvents));
    }

    /**
     * 格式化模型响应：保留 JSON 的全部结构和元数据，仅缩短模型文本输出字段。
     *
     * @param objectMapper JSON 序列化工具
     * @param responseBody 原始响应内容
     * @return 适合日志输出的响应内容
     */
    static String formatResponse(ObjectMapper objectMapper, String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            if (!(root instanceof ObjectNode response)) {
                return responseBody;
            }
            truncateChatCompletionsOutput(response);
            truncateOpenAiResponsesOutput(response);
            return objectMapper.writeValueAsString(response);
        } catch (Exception ignored) {
            return responseBody;
        }
    }

    /**
     * 格式化流式响应事件，并在全部事件范围内限制模型文本长度。
     *
     * @param objectMapper JSON 序列化工具
     * @param responseEvents SSE 的 data 内容集合
     * @return 适合日志输出的流式响应内容
     */
    static String formatStreamResponse(ObjectMapper objectMapper, List<String> responseEvents) {
        try {
            ArrayNode events = objectMapper.createArrayNode();
            int remainingTextLength = MODEL_TEXT_LENGTH;
            for (String responseEvent : responseEvents) {
                try {
                    JsonNode event = objectMapper.readTree(responseEvent);
                    if (event instanceof ObjectNode eventNode) {
                        remainingTextLength = truncateStreamOutput(eventNode, remainingTextLength);
                    }
                    events.add(event);
                } catch (Exception ignored) {
                    events.add(responseEvent);
                }
            }
            return objectMapper.writeValueAsString(events);
        } catch (Exception ignored) {
            return responseEvents.toString();
        }
    }

    /**
     * 截断 Chat Completions 协议中的普通与流式文本输出字段。
     *
     * @param response Chat Completions 响应 JSON
     */
    private static void truncateChatCompletionsOutput(ObjectNode response) {
        for (JsonNode choice : response.path("choices")) {
            if (!(choice instanceof ObjectNode choiceNode)) {
                continue;
            }
            truncateTextField(choiceNode.path("message"), "content");
            truncateTextField(choiceNode.path("delta"), "content");
        }
    }

    /**
     * 截断 OpenAI Responses API 中的文本输出字段。
     *
     * @param response OpenAI 响应 JSON
     */
    private static void truncateOpenAiResponsesOutput(ObjectNode response) {
        truncateTextField(response, "output_text");
        if ("response.output_text.delta".equals(response.path("type").asText())) {
            truncateTextField(response, "delta");
        }
        for (JsonNode output : response.path("output")) {
            if (!(output instanceof ObjectNode outputNode)) {
                continue;
            }
            for (JsonNode content : outputNode.path("content")) {
                if (content instanceof ObjectNode contentNode && "output_text".equals(contentNode.path("type").asText())) {
                    truncateTextField(contentNode, "text");
                }
            }
        }
    }

    /**
     * 截断流式事件中的文本输出，并返回剩余可记录的文本长度。
     *
     * @param event SSE 事件 JSON
     * @param remainingTextLength 剩余可记录的模型文本长度
     * @return 更新后的剩余文本长度
     */
    private static int truncateStreamOutput(ObjectNode event, int remainingTextLength) {
        for (JsonNode choice : event.path("choices")) {
            if (choice instanceof ObjectNode choiceNode) {
                remainingTextLength = truncateTextField(choiceNode.path("delta"), "content", remainingTextLength);
            }
        }
        if ("response.output_text.delta".equals(event.path("type").asText())) {
            remainingTextLength = truncateTextField(event, "delta", remainingTextLength);
        }
        return remainingTextLength;
    }

    /**
     * 截断 JSON 对象中指定的文本字段。
     *
     * @param node JSON 对象节点
     * @param fieldName 文本字段名称
     */
    private static void truncateTextField(JsonNode node, String fieldName) {
        if (node instanceof ObjectNode objectNode && objectNode.path(fieldName).isTextual()) {
            objectNode.put(fieldName, truncateModelText(objectNode.path(fieldName).asText()));
        }
    }

    /**
     * 截断流式 JSON 对象中的文本字段，并扣减可记录的文本长度。
     *
     * @param node JSON 对象节点
     * @param fieldName 文本字段名称
     * @param remainingTextLength 剩余可记录的模型文本长度
     * @return 更新后的剩余文本长度
     */
    private static int truncateTextField(JsonNode node, String fieldName, int remainingTextLength) {
        if (!(node instanceof ObjectNode objectNode) || !objectNode.path(fieldName).isTextual()) {
            return remainingTextLength;
        }
        String content = objectNode.path(fieldName).asText();
        int length = Math.min(remainingTextLength, content.length());
        objectNode.put(fieldName, content.substring(0, length));
        return remainingTextLength - length;
    }

    /**
     * 截取模型文本的前 50 个字符，并标识已省略后续内容。
     *
     * @param content 模型原始文本
     * @return 用于日志的模型文本
     */
    private static String truncateModelText(String content) {
        if (content.length() <= MODEL_TEXT_LENGTH) {
            return content;
        }
        return content.substring(0, MODEL_TEXT_LENGTH) + "...";
    }
}
