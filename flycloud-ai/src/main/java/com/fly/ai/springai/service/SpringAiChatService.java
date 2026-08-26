package com.fly.ai.springai.service;

import com.fly.ai.model.AiChatRequest;
import com.fly.ai.model.AiChatResponse;
import com.fly.ai.original.config.AiProperties;
import com.fly.ai.springai.utils.SpringAiChatUtils;
import com.fly.ai.utils.AiUtils;
import com.fly.common.exception.AiProviderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 基于 Spring AI {@link ChatClient} 的聊天服务。
 * <p>
 * 通过 {@link SpringAiModelProviderRouter} 与原生实现共同按 {@code flycloud.ai.provider} 路由。
 * Controller 与 DTO 不暴露 Spring AI 类型，以便与原生 HTTP 实现保持相同的对外契约。
 *
 * @author lxs
 * @date 2026-08-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpringAiChatService {

    private final AiProperties aiProperties;

    private final SpringAiModelProviderRouter providerRouter;

    /**
     * 使用 Spring AI 发起普通聊天请求。
     *
     * @param request 聊天请求
     * @return 统一聊天响应
     */
    public AiChatResponse chat(AiChatRequest request) {
        AiUtils.requireServiceEnabled(aiProperties.isEnabled());
        SpringAiModelProviderRouter.SelectedChatClient selected = providerRouter.getSelectedChatClient();
        log.info("Spring AI 模型请求，provider={}, message={}, model={}, maxOutputTokens={}",
                selected.providerName(), request.message(), request.model(), request.maxOutputTokens());
        try {
            ChatResponse response = SpringAiChatUtils.requestSpec(selected.chatClient(), request).call().chatResponse();
            AiChatResponse result = SpringAiChatUtils.toChatResponse(response);
            log.info("Spring AI 模型响应，provider={}, responseId={}, model={}, usage={}, contentPreview={}",
                    selected.providerName(), result.responseId(), result.model(), result.usage(),
                    AiUtils.previewModelContent(result.content()));
            return result;
        } catch (AiProviderException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            log.error("Spring AI 模型调用失败，provider={}", selected.providerName(), exception);
            throw new AiProviderException(502, "Spring AI 调用模型服务失败", exception);
        }
    }

    /**
     * 使用 Spring AI 发起流式聊天请求，并转换为项目统一 SSE 事件。
     *
     * @param request 聊天请求
     * @return SSE 响应发送器
     */
    public SseEmitter stream(AiChatRequest request) {
        AiUtils.requireServiceEnabled(aiProperties.isEnabled());
        SpringAiModelProviderRouter.SelectedChatClient selected = providerRouter.getSelectedChatClient();
        log.info("Spring AI 流式模型请求，provider={}, message={}, model={}, maxOutputTokens={}",
                selected.providerName(), request.message(), request.model(), request.maxOutputTokens());
        SseEmitter emitter = new SseEmitter(0L);
        AtomicReference<ChatResponseMetadata> lastMetadata = new AtomicReference<>();
        StringBuilder contentPreview = new StringBuilder();
        try {
            SpringAiChatUtils.requestSpec(selected.chatClient(), request).stream().chatResponse().subscribe(
                    response -> SpringAiChatUtils.handleStreamResponse(emitter, response, lastMetadata, contentPreview),
                    exception -> SpringAiChatUtils.handleStreamError(emitter, selected.providerName(), exception),
                    () -> SpringAiChatUtils.completeStream(emitter, selected.providerName(), lastMetadata.get(), contentPreview));
        } catch (RuntimeException exception) {
            SpringAiChatUtils.handleStreamError(emitter, selected.providerName(), exception);
        }
        return emitter;
    }

}
