package com.fly.ai.chat.service;

import com.fly.ai.chat.model.AiUnifiedChatRequest;
import com.fly.ai.chat.model.AiUnifiedChatResponse;
import com.fly.ai.common.model.AiStreamEvent;
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

/**
 * 正式统一 AI 聊天编排服务。
 * <p>
 * 当前组合 Chat Memory、RAG 与既有 Tool Calling。后续 Agent、MCP 将在此编排链增加能力选择，复用相同
 * 会话、消息、知识库和工具组件，而非复制一套聊天服务。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Service
@RequiredArgsConstructor
public class AiUnifiedChatService {

    private final AiConversationService conversationService;

    private final AiAgentService agentService;

    private final AiRagService ragService;

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
        try {
            AiRagContext ragContext = ragService.retrieveContext(request.message());
            AiAgentResponse agentResponse = agentService.chat(request.toChatRequest(), loginUserId,
                    conversationId, ragContext);
            AiToolCallingResponse response = agentResponse.response();
            conversationService.saveAssistantMessage(conversationId, loginUserId, response,
                    aiProperties.getProvider().getValue(), agentResponse.knowledgeReferences());
            return toUnifiedResponse(conversationId, response, agentResponse.knowledgeReferences());
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
        AiRagContext ragContext;
        try {
            ragContext = ragService.retrieveContext(request.message());
        } catch (RuntimeException exception) {
            conversationService.saveFailedAssistantMessage(conversationId, loginUserId);
            throw exception;
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
}
