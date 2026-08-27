package com.fly.ai.common.utils;

import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.model.AiChatResponse;
import com.fly.ai.common.model.AiStreamEvent;
import com.fly.ai.common.model.AiUsage;
import com.fly.common.utils.ai.AiUtils;
import com.fly.common.exception.AiProviderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Spring AI 聊天接口的请求、响应和 SSE 适配工具。
 *
 * @author lxs
 * @date 2026-08-26
 */
@Slf4j
public final class SpringAiChatUtils {

    private SpringAiChatUtils() {
    }

    /**
     * 构造当前聊天请求对应的 Spring AI 调用规格。
     *
     * @param chatClient 当前供应商的 Spring AI 聊天客户端
     * @param request 聊天请求
     * @return Spring AI 请求规格
     */
    public static ChatClient.ChatClientRequestSpec requestSpec(ChatClient chatClient, AiChatRequest request) {
        ChatClient.ChatClientRequestSpec requestSpec = chatClient.prompt().user(request.message());
        if (AiUtils.hasText(request.model()) || request.maxOutputTokens() != null) {
            requestSpec.options(chatOptions(request));
        }
        return requestSpec;
    }

    /**
     * 将 Spring AI 响应转换为项目统一响应。
     *
     * @param response Spring AI 响应
     * @return 项目聊天响应
     */
    public static AiChatResponse toChatResponse(ChatResponse response) {
        if (response == null || response.getResult() == null || response.getResult().getOutput() == null) {
            throw new AiProviderException(502, "Spring AI 未返回有效的模型响应");
        }
        ChatResponseMetadata metadata = response.getMetadata();
        return new AiChatResponse(metadata.getId(), metadata.getModel(), response.getResult().getOutput().getText(),
                toUsage(metadata.getUsage()));
    }

    /**
     * 处理单个 Spring AI 流式响应分片。
     *
     * @param emitter SSE 发送器
     * @param response Spring AI 响应分片
     * @param lastMetadata 最近一次响应元数据
     * @param contentPreview 日志文本预览缓冲区
     */
    public static void handleStreamResponse(SseEmitter emitter, ChatResponse response,
            AtomicReference<ChatResponseMetadata> lastMetadata, StringBuilder contentPreview) {
        if (response == null || response.getResult() == null || response.getResult().getOutput() == null) {
            return;
        }
        lastMetadata.set(response.getMetadata());
        String delta = response.getResult().getOutput().getText();
        AiUtils.appendModelContentPreview(contentPreview, delta);
        if (AiUtils.hasText(delta)) {
            sendEvent(emitter, AiStreamEvent.delta(delta));
        }
    }

    /**
     * 结束流式响应并发送完成事件。
     *
     * @param emitter SSE 发送器
     * @param providerName 供应商展示名称
     * @param metadata 最后一次响应元数据
     * @param contentPreview 日志文本预览缓冲区
     */
    public static void completeStream(SseEmitter emitter, String providerName, ChatResponseMetadata metadata,
            StringBuilder contentPreview) {
        String responseId = metadata == null ? null : metadata.getId();
        AiUsage usage = metadata == null ? null : toUsage(metadata.getUsage());
        try {
            sendEvent(emitter, AiStreamEvent.completed(responseId, usage));
            log.info("Spring AI 流式模型响应，provider={}, responseId={}, model={}, usage={}, contentPreview={}",
                    providerName, responseId, metadata == null ? null : metadata.getModel(), usage, contentPreview);
            emitter.complete();
        } catch (Exception exception) {
            emitter.completeWithError(exception);
        }
    }

    /**
     * 记录并向客户端发送流式调用错误事件。
     *
     * @param emitter SSE 发送器
     * @param providerName 供应商展示名称
     * @param exception 调用异常
     */
    public static void handleStreamError(SseEmitter emitter, String providerName, Throwable exception) {
        log.error("Spring AI 流式模型调用失败，provider={}", providerName, exception);
        try {
            sendEvent(emitter, AiStreamEvent.error("Spring AI 调用模型服务失败"));
            emitter.complete();
        } catch (Exception sendException) {
            emitter.completeWithError(sendException);
        }
    }

    /**
     * 向 SSE 客户端发送统一事件。
     * <p>
     * 除普通 Spring AI 聊天外，Tool Calling 等上层编排也需要复用同一事件协议，避免前端为不同
     * 聊天模式维护多套 SSE 解析逻辑。
     *
     * @param emitter SSE 发送器
     * @param event SSE 事件
     */
    public static void sendStreamEvent(SseEmitter emitter, AiStreamEvent event) {
        sendEvent(emitter, event);
    }

    /**
     * 构造本次请求的通用模型选项。
     *
     * @param request 聊天请求
     * @return 通用模型选项
     */
    private static ChatOptions chatOptions(AiChatRequest request) {
        ChatOptions.Builder builder = ChatOptions.builder();
        if (AiUtils.hasText(request.model())) {
            builder.model(request.model());
        }
        if (request.maxOutputTokens() != null) {
            builder.maxTokens(request.maxOutputTokens());
        }
        return builder.build();
    }

    /**
     * 将 Spring AI Token 用量转换为项目统一用量对象。
     *
     * @param usage Spring AI 用量对象
     * @return 项目 Token 用量
     */
    private static AiUsage toUsage(Usage usage) {
        if (usage == null || usage.getTotalTokens() == null || usage.getTotalTokens() <= 0) {
            return null;
        }
        return new AiUsage(AiUtils.valueOrZero(usage.getPromptTokens()),
                AiUtils.valueOrZero(usage.getCompletionTokens()), AiUtils.valueOrZero(usage.getTotalTokens()));
    }

    /**
     * 向客户端发送统一 SSE 事件。
     *
     * @param emitter SSE 发送器
     * @param event SSE 事件
     */
    private static void sendEvent(SseEmitter emitter, AiStreamEvent event) {
        try {
            emitter.send(SseEmitter.event().name(event.type()).data(event, MediaType.APPLICATION_JSON));
        } catch (IOException exception) {
            throw new AiProviderException(499, "客户端已断开 Spring AI 流式连接", exception);
        }
    }
}
