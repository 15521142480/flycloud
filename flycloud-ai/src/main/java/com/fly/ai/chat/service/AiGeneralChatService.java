package com.fly.ai.chat.service;

import com.fly.ai.common.config.AiProperties;
import com.fly.ai.common.config.AiRuntimeContextService;
import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.model.AiChatResponse;
import com.fly.ai.common.springai.SpringAiModelProviderRouter;
import com.fly.ai.common.utils.SpringAiChatUtils;
import com.fly.common.exception.AiProviderException;
import com.fly.common.utils.ai.AiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 正式统一聊天的纯模型调用服务。
 * <p>
 * 用于通用问答和仅需 RAG 的规则问答：保留 Chat Memory，但绝不向模型暴露订单、用户等业务工具。
 *
 * @author lxs
 * @date 2026-09-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiGeneralChatService {

    private static final String GENERAL_SYSTEM_POLICY = """
            你是飞翔云系统的智能助手。除处理受保护业务数据时由系统另行提供工具外，你可以直接回答通用技术、
            产品和学习类问题。对于“当前”“最新”等可能变化的模型价格、产品能力、政策或版本信息，请明确说明
            你的回答可能不是实时数据，并建议用户以相应供应商的官方页面或已接入的可信数据源为准；不得声称已联网
            查询或掌握未经提供的实时价格。
            对天气、行情、汇率、新闻、航班、交通等实时外部事实，只有系统通过受控 Tool 或可信数据源提供结果时才可
            回答具体数据；未提供时必须明确说明无法获得实时结果，不得编造。
            """;

    private final AiProperties aiProperties;

    private final AiRuntimeContextService runtimeContextService;

    private final SpringAiModelProviderRouter providerRouter;

    private final ChatMemory aiChatMemory;

    /**
     * 发起保留短期会话记忆的纯模型聊天。
     *
     * @param request 聊天请求
     * @param conversationId 会话编号
     * @param supplementalSystemPrompt 经过相关度筛选的 RAG 上下文；为空时不注入
     * @return 模型响应
     */
    public AiChatResponse chat(AiChatRequest request, String conversationId, String supplementalSystemPrompt) {
        AiUtils.requireServiceEnabled(aiProperties.isEnabled());
        SpringAiModelProviderRouter.SelectedChatClient selected = providerRouter.getSelectedChatClient();
        log.info("AI 通用聊天请求，provider={}, conversationId={}, message={}, model={}, maxOutputTokens={}",
                selected.providerName(), conversationId, request.message(), request.model(), request.maxOutputTokens());
        try {
            ChatResponse response = SpringAiChatUtils.withConversationMemory(
                            SpringAiChatUtils.requestSpec(selected.chatClient(), request), aiChatMemory, aiProperties, conversationId)
                    .system(systemPrompt(supplementalSystemPrompt))
                    .call()
                    .chatResponse();
            AiChatResponse result = SpringAiChatUtils.toChatResponse(response);
            log.info("AI 通用聊天响应，provider={}, responseId={}, model={}, usage={}, contentPreview={}",
                    selected.providerName(), result.responseId(), result.model(), result.usage(),
                    AiUtils.previewModelContent(result.content()));
            return result;
        } catch (AiProviderException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            log.error("AI 通用聊天调用失败，provider={}, conversationId={}", selected.providerName(), conversationId, exception);
            throw new AiProviderException(502, "AI 通用聊天调用模型服务失败", exception);
        }
    }

    /**
     * 发起纯模型流式聊天。
     *
     * @param request 聊天请求
     * @param conversationId 会话编号
     * @param supplementalSystemPrompt 经过相关度筛选的 RAG 上下文；为空时不注入
     * @param observer 流式生命周期回调
     * @return SSE 响应发送器
     */
    public SseEmitter stream(AiChatRequest request, String conversationId, String supplementalSystemPrompt,
            AiGeneralChatStreamObserver observer) {
        AiUtils.requireServiceEnabled(aiProperties.isEnabled());
        SpringAiModelProviderRouter.SelectedChatClient selected = providerRouter.getSelectedChatClient();
        SseEmitter emitter = new SseEmitter(0L);
        AtomicReference<ChatResponseMetadata> lastMetadata = new AtomicReference<>();
        StringBuilder contentPreview = new StringBuilder();
        StringBuilder content = new StringBuilder();
        log.info("AI 通用流式聊天请求，provider={}, conversationId={}, message={}, model={}, maxOutputTokens={}",
                selected.providerName(), conversationId, request.message(), request.model(), request.maxOutputTokens());
        try {
            observer.onStarted(emitter);
            SpringAiChatUtils.withConversationMemory(
                            SpringAiChatUtils.requestSpec(selected.chatClient(), request), aiChatMemory, aiProperties, conversationId)
                    .system(systemPrompt(supplementalSystemPrompt))
                    .stream()
                    .chatResponse()
                    .subscribe(
                            response -> handleStreamResponse(emitter, response, lastMetadata, contentPreview, content),
                            exception -> handleStreamError(emitter, selected.providerName(), exception, observer),
                            () -> completeStream(emitter, selected.providerName(), lastMetadata.get(), contentPreview, content, observer));
        } catch (RuntimeException exception) {
            handleStreamError(emitter, selected.providerName(), exception, observer);
        }
        return emitter;
    }

    /**
     * 处理模型响应分片。
     *
     * @param emitter SSE 响应发送器
     * @param response 模型响应分片
     * @param lastMetadata 最近一次模型响应元数据
     * @param contentPreview 日志预览文本
     * @param content 完整模型文本
     */
    private void handleStreamResponse(SseEmitter emitter, ChatResponse response,
            AtomicReference<ChatResponseMetadata> lastMetadata, StringBuilder contentPreview, StringBuilder content) {
        String delta = SpringAiChatUtils.handleStreamResponse(emitter, response, lastMetadata, contentPreview);
        if (AiUtils.hasText(delta)) {
            content.append(delta);
        }
    }

    /**
     * 处理模型调用失败。
     *
     * @param emitter SSE 响应发送器
     * @param providerName 模型供应商名称
     * @param exception 异常原因
     * @param observer 流式生命周期回调
     */
    private void handleStreamError(SseEmitter emitter, String providerName, Throwable exception,
            AiGeneralChatStreamObserver observer) {
        observer.onError(exception);
        SpringAiChatUtils.handleStreamError(emitter, providerName, exception);
    }

    /**
     * 完成模型流并回传聚合后的模型响应。
     *
     * @param emitter SSE 响应发送器
     * @param providerName 模型供应商名称
     * @param metadata 最近一次模型响应元数据
     * @param contentPreview 日志预览文本
     * @param content 完整模型文本
     * @param observer 流式生命周期回调
     */
    private void completeStream(SseEmitter emitter, String providerName, ChatResponseMetadata metadata,
            StringBuilder contentPreview, StringBuilder content, AiGeneralChatStreamObserver observer) {
        AiChatResponse response = new AiChatResponse(metadata == null ? null : metadata.getId(),
                metadata == null ? null : metadata.getModel(), content.toString(),
                metadata == null ? null : SpringAiChatUtils.toUsage(metadata.getUsage()));
        observer.onCompleted(response);
        SpringAiChatUtils.completeStream(emitter, providerName, metadata, contentPreview);
    }

    /**
     * 构建纯模型聊天的系统提示词。
     *
     * @param supplementalSystemPrompt 已筛选的知识库上下文
     * @return 系统提示词
     */
    private String systemPrompt(String supplementalSystemPrompt) {
        return runtimeContextService.systemPrompt(GENERAL_SYSTEM_POLICY, supplementalSystemPrompt);
    }
}
