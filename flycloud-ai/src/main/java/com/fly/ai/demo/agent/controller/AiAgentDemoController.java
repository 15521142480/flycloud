package com.fly.ai.demo.agent.controller;

import com.fly.ai.common.agent.model.AiAgentResponse;
import com.fly.ai.common.agent.service.AiAgentService;
import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.tool.service.AiToolCallingStreamObserver;
import com.fly.ai.common.utils.AiSecurityUtils;
import com.fly.common.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 第 8 步 Agent 学习测试控制器。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Tag(name = "AI Agent 测试")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/demo/agent")
public class AiAgentDemoController {

    private final AiAgentService agentService;

    /**
     * 测试由 Agent 自动组合 RAG 和多个受控业务工具的非流式问答。
     *
     * @param request 聊天请求
     * @return Agent 执行结果
     */
    @Operation(summary = "Agent 聊天测试")
    @PostMapping("/chat")
    public R<AiAgentResponse> chat(@Valid @RequestBody AiChatRequest request) {
        return R.ok(agentService.chat(request, AiSecurityUtils.requiredLoginUserId("AI Agent"), null));
    }

    /**
     * 测试由 Agent 自动组合 RAG 和多个受控业务工具的流式问答。
     *
     * @param request 聊天请求
     * @return SSE 发送器
     */
    @Operation(summary = "Agent 流式聊天测试")
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@Valid @RequestBody AiChatRequest request) {
        return agentService.stream(request, AiSecurityUtils.requiredLoginUserId("AI Agent"), null,
                new AiToolCallingStreamObserver() {
                });
    }
}
