package com.fly.ai.common.agent.service;

import com.fly.ai.common.agent.model.AiAgentResponse;
import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.rag.model.AiRagContext;
import com.fly.ai.common.rag.service.AiRagService;
import com.fly.ai.common.tool.model.AiToolCallingResponse;
import com.fly.ai.common.tool.service.AiToolCallingChatService;
import com.fly.ai.common.tool.service.AiToolCallingStreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 统一 AI Agent 编排服务。
 * <p>
 * Agent 不复制工具、记忆或知识库实现：先复用 RAG 获取事实上下文，再交由既有 Spring AI Tool Calling
 * 执行多轮工具调用。Spring AI 会在模型请求工具、获取工具结果、继续推理之间自动循环，直到得到最终回答。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Service
@RequiredArgsConstructor
public class AiAgentService {

    private static final String AGENT_SYSTEM_POLICY = """
            你是飞翔云系统的受控业务 Agent。面对复合问题时，请先拆解需要确认的事实，再按需查询知识库和调用业务工具，
            可连续调用多个工具，最后基于真实结果给出清晰结论。只具备查询能力，不能执行创建、修改、删除、退款等动作；
            任何订单、用户信息和权限结论均必须以工具返回结果为准。
            """;

    private final AiRagService ragService;

    private final AiToolCallingChatService toolCallingChatService;

    /**
     * 执行 Agent 非流式聊天。
     *
     * @param request 聊天请求
     * @param loginUserId 当前登录用户编号
     * @param conversationId 会话编号；为空时不使用短期记忆
     * @return Agent 结果
     */
    public AiAgentResponse chat(AiChatRequest request, Long loginUserId, String conversationId) {
        return chat(request, loginUserId, conversationId, ragService.retrieveContext(request.message()));
    }

    /**
     * 使用已完成的检索上下文执行 Agent 非流式聊天，供统一聊天在同一轮同时保存引用审计信息。
     *
     * @param request 聊天请求
     * @param loginUserId 当前登录用户编号
     * @param conversationId 会话编号；为空时不使用短期记忆
     * @param ragContext 当前轮已获取的 RAG 上下文
     * @return Agent 结果
     */
    public AiAgentResponse chat(AiChatRequest request, Long loginUserId, String conversationId, AiRagContext ragContext) {
        AiToolCallingResponse response = toolCallingChatService.chat(request, loginUserId, conversationId,
                agentSystemPrompt(ragContext.systemPrompt()));
        return new AiAgentResponse(response, ragContext.references());
    }

    /**
     * 执行 Agent 流式聊天。
     *
     * @param request 聊天请求
     * @param loginUserId 当前登录用户编号
     * @param conversationId 会话编号；为空时不使用短期记忆
     * @param observer 流式生命周期观察器
     * @return SSE 发送器
     */
    public SseEmitter stream(AiChatRequest request, Long loginUserId, String conversationId,
            AiToolCallingStreamObserver observer) {
        return stream(request, loginUserId, conversationId, ragService.retrieveContext(request.message()), observer);
    }

    /**
     * 使用已完成的检索上下文执行 Agent 流式聊天，供统一聊天保存同一轮的引用审计信息。
     *
     * @param request 聊天请求
     * @param loginUserId 当前登录用户编号
     * @param conversationId 会话编号；为空时不使用短期记忆
     * @param ragContext 当前轮已获取的 RAG 上下文
     * @param observer 流式生命周期观察器
     * @return SSE 发送器
     */
    public SseEmitter stream(AiChatRequest request, Long loginUserId, String conversationId, AiRagContext ragContext,
            AiToolCallingStreamObserver observer) {
        return toolCallingChatService.stream(request, loginUserId, conversationId, observer,
                agentSystemPrompt(ragContext.systemPrompt()));
    }

    /**
     * 合并 Agent 行为约束和 RAG 的真实检索上下文。
     *
     * @param ragSystemPrompt RAG 上下文
     * @return 受控系统提示词
     */
    private String agentSystemPrompt(String ragSystemPrompt) {
        return ragSystemPrompt == null || ragSystemPrompt.isBlank() ? AGENT_SYSTEM_POLICY
                : AGENT_SYSTEM_POLICY + "\n\n" + ragSystemPrompt;
    }
}
