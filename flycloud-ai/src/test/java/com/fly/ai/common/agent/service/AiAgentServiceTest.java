package com.fly.ai.common.agent.service;

import com.fly.ai.common.agent.model.AiAgentResponse;
import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.rag.model.AiRagContext;
import com.fly.ai.common.rag.service.AiRagService;
import com.fly.ai.common.tool.model.AiToolCallingResponse;
import com.fly.ai.common.tool.service.AiToolCallingChatService;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 验证 Agent 学习 Demo 使用真实 RAG TopK 上下文后再复用 Tool Calling。
 */
class AiAgentServiceTest {

    @Test
    void shouldComposeDemoKnowledgeAndToolCalling() {
        AiChatRequest request = new AiChatRequest("退款规定是什么？", null, null);
        AiRagContext context = new AiRagContext("退款知识上下文", Collections.emptyList());
        AiRagService ragService = mock(AiRagService.class);
        AiToolCallingChatService toolCallingChatService = mock(AiToolCallingChatService.class);
        AiToolCallingResponse toolResponse = new AiToolCallingResponse("response-1", "qwen-plus", "可以退款", null,
                null, Collections.emptyList());
        when(ragService.retrieveContextForDemo(request.message())).thenReturn(context);
        when(toolCallingChatService.chat(eq(request), eq(1L), isNull(), contains(context.systemPrompt())))
                .thenReturn(toolResponse);
        AiAgentService service = new AiAgentService(ragService, toolCallingChatService);

        AiAgentResponse response = service.chatForDemo(request, 1L);

        assertSame(toolResponse, response.response());
        verify(ragService).retrieveContextForDemo(request.message());
        verify(toolCallingChatService).chat(eq(request), eq(1L), isNull(), contains(context.systemPrompt()));
    }
}
