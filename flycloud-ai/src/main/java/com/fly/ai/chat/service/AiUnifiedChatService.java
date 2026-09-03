package com.fly.ai.chat.service;

import com.fly.ai.chat.model.AiUnifiedChatRequest;
import com.fly.ai.chat.model.AiUnifiedChatResponse;
import com.fly.ai.common.model.AiStreamEvent;
import com.fly.ai.common.model.AiChatResponse;
import com.fly.ai.common.utils.SpringAiChatUtils;
import com.fly.ai.common.config.AiProperties;
import com.fly.ai.common.tool.model.AiToolCallingResponse;
import com.fly.ai.common.agent.model.AiAgentResponse;
import com.fly.ai.common.agent.service.AiAgentService;
import com.fly.ai.common.tool.service.AiToolCallingStreamObserver;
import com.fly.ai.common.rag.model.AiRagContext;
import com.fly.ai.common.rag.service.AiRagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;

/**
 * 正式统一 AI 聊天编排服务。
 * <p>
 * 每轮先按意图选择能力：通用问题仅使用 Chat Memory 与模型；业务查询才挂载既有 Tool Calling；公司规则
 * 问题才尝试 RAG。后续 Agent、MCP 将在此编排链增加能力选择，复用相同会话、消息、知识库和工具组件，
 * 而非复制一套聊天服务。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Service
@RequiredArgsConstructor
public class AiUnifiedChatService {

    private final AiConversationService conversationService;

    private final AiAgentService agentService;

    private final AiGeneralChatService generalChatService;

    private final AiRagService ragService;

    private final AiUnifiedChatIntentRouter intentRouter;

    private final AiProperties aiProperties;

    /**
     * 执行正式统一非流式聊天。
     *
     * @param request 统一聊天请求
     * @param loginUserId 当前登录用户编号
     * @return 统一聊天响应
     */
    public AiUnifiedChatResponse chat(AiUnifiedChatRequest request, Long loginUserId) {
        String conversationId = conversationService.prepareTurn(request.conversationId(), loginUserId, request.message());
        AiUnifiedChatIntent intent = intentRouter.route(request.message());
        try {
            AiRagContext ragContext = ragContext(intent, request.message());
            if (intent.isToolCallingEnabled()) {
                AiAgentResponse agentResponse = agentService.chat(request.toChatRequest(), loginUserId,
                        conversationId, ragContext);
                AiToolCallingResponse response = agentResponse.response();
                conversationService.saveAssistantMessage(conversationId, loginUserId, response,
                        aiProperties.getProvider().getValue(), agentResponse.knowledgeReferences());
                return toUnifiedResponse(conversationId, response, agentResponse.knowledgeReferences());
            }
            AiChatResponse response = generalChatService.chat(request.toChatRequest(), conversationId,
                    ragContext.systemPrompt());
            conversationService.saveAssistantMessage(conversationId, loginUserId, response,
                    aiProperties.getProvider().getValue(), ragContext.references());
            return toUnifiedResponse(conversationId, response, ragContext.references());
        } catch (RuntimeException exception) {
            conversationService.saveFailedAssistantMessage(conversationId, loginUserId);
            throw exception;
        }
    }

    /**
     * 执行正式统一流式聊天。
     *
     * @param request 统一聊天请求
     * @param loginUserId 当前登录用户编号
     * @return SSE 发送器
     */
    public SseEmitter stream(AiUnifiedChatRequest request, Long loginUserId) {
        String conversationId = conversationService.prepareTurn(request.conversationId(), loginUserId, request.message());
        AiUnifiedChatIntent intent = intentRouter.route(request.message());
        AiRagContext ragContext;
        try {
            ragContext = ragContext(intent, request.message());
        } catch (RuntimeException exception) {
            conversationService.saveFailedAssistantMessage(conversationId, loginUserId);
            throw exception;
        }
        if (!intent.isToolCallingEnabled()) {
            return generalChatService.stream(request.toChatRequest(), conversationId, ragContext.systemPrompt(),
                    new AiGeneralChatStreamObserver() {

                        @Override
                        public void onStarted(SseEmitter emitter) {
                            SpringAiChatUtils.sendStreamEvent(emitter, AiStreamEvent.conversation(conversationId));
                        }

                        @Override
                        public void onCompleted(AiChatResponse response) {
                            conversationService.saveAssistantMessage(conversationId, loginUserId, response,
                                    aiProperties.getProvider().getValue(), ragContext.references());
                        }

                        @Override
                        public void onError(Throwable exception) {
                            conversationService.saveFailedAssistantMessage(conversationId, loginUserId);
                        }
                    });
        }
        return agentService.stream(request.toChatRequest(), loginUserId, conversationId, ragContext,
                new AiToolCallingStreamObserver() {

                    @Override
                    public void onStarted(SseEmitter emitter) {
                        SpringAiChatUtils.sendStreamEvent(emitter, AiStreamEvent.conversation(conversationId));
                    }

                    @Override
                    public void onCompleted(AiToolCallingResponse response) {
                        conversationService.saveAssistantMessage(conversationId, loginUserId, response,
                                aiProperties.getProvider().getValue(), ragContext.references());
                    }

                    @Override
                    public void onError(Throwable exception) {
                        conversationService.saveFailedAssistantMessage(conversationId, loginUserId);
                    }
                });
    }

    /**
     * 将 Tool Calling 响应转换为正式统一接口响应。
     *
     * @param conversationId 会话编号
     * @param response Tool Calling 响应
     * @return 统一聊天响应
     */
    private AiUnifiedChatResponse toUnifiedResponse(String conversationId, AiToolCallingResponse response,
            java.util.List<com.fly.ai.common.knowledge.model.AiKnowledgeHit> knowledgeReferences) {
        return new AiUnifiedChatResponse(conversationId, response.responseId(), response.model(), response.content(),
                response.usage(), response.permission(), response.toolNames(), knowledgeReferences);
    }

    /**
     * 将纯模型响应转换为统一接口响应。
     *
     * @param conversationId 会话编号
     * @param response 模型响应
     * @param knowledgeReferences 本轮实际命中的知识库片段
     * @return 统一聊天响应
     */
    private AiUnifiedChatResponse toUnifiedResponse(String conversationId, AiChatResponse response,
            java.util.List<com.fly.ai.common.knowledge.model.AiKnowledgeHit> knowledgeReferences) {
        return new AiUnifiedChatResponse(conversationId, response.responseId(), response.model(), response.content(),
                response.usage(), null, Collections.emptyList(), knowledgeReferences);
    }

    /**
     * 仅在本轮意图需要公司规则知识时进行检索。
     *
     * @param intent 本轮统一聊天意图
     * @param message 用户消息
     * @return 真实检索上下文；普通问题不访问向量数据库
     */
    private AiRagContext ragContext(AiUnifiedChatIntent intent, String message) {
        if (!intent.isRagEnabled()) {
            return new AiRagContext("", Collections.emptyList());
        }
        return ragService.retrieveContext(message);
    }
}
