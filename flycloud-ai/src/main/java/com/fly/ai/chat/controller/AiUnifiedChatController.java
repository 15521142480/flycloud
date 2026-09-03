package com.fly.ai.chat.controller;

import com.fly.ai.chat.model.AiChatHistoryMessage;
import com.fly.ai.chat.model.AiConversationSummary;
import com.fly.ai.chat.model.AiUnifiedChatRequest;
import com.fly.ai.chat.model.AiUnifiedChatResponse;
import com.fly.ai.chat.service.AiConversationService;
import com.fly.ai.chat.service.AiUnifiedChatService;
import com.fly.ai.common.utils.AiSecurityUtils;
import com.fly.common.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * 正式统一 AI 聊天控制器。
 * <p>
 * 该入口面向正式业务，统一承载会话、短期记忆和现有受控工具能力；学习过程的接口保留在 {@code /ai/demo/**}。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Tag(name = "AI 统一聊天")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/chat")
public class AiUnifiedChatController {

    private final AiUnifiedChatService unifiedChatService;

    private final AiConversationService conversationService;

    /**
     * 发起正式统一非流式聊天。
     *
     * @param request 聊天请求
     * @return 含会话编号的模型响应
     */
    @Operation(summary = "统一 AI 聊天", description = "自动创建或续接会话；按问题意图启用 Chat Memory、受控工具或 RAG")
    @PostMapping
    public R<AiUnifiedChatResponse> chat(@Valid @RequestBody AiUnifiedChatRequest request) {
        return R.ok(unifiedChatService.chat(request, AiSecurityUtils.requiredLoginUserId("AI 聊天")));
    }

    /**
     * 发起正式统一流式聊天。
     *
     * @param request 聊天请求
     * @return SSE 发送器；首个 conversation 事件携带服务端会话编号
     */
    @Operation(summary = "统一 AI 流式聊天", description = "自动创建或续接会话，按问题意图启用能力并通过 SSE 返回结果")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@Valid @RequestBody AiUnifiedChatRequest request) {
        return unifiedChatService.stream(request, AiSecurityUtils.requiredLoginUserId("AI 聊天"));
    }

    /**
     * 查询当前用户的历史会话。
     *
     * @return 会话摘要列表
     */
    @Operation(summary = "查询 AI 历史会话")
    @GetMapping("/conversations")
    public R<List<AiConversationSummary>> conversations() {
        return R.ok(conversationService.listConversations(AiSecurityUtils.requiredLoginUserId("AI 聊天")));
    }

    /**
     * 查询当前用户指定会话的完整历史消息。
     *
     * @param conversationId 会话编号
     * @return 历史消息
     */
    @Operation(summary = "查询 AI 会话消息")
    @GetMapping("/conversations/{conversationId}/messages")
    public R<List<AiChatHistoryMessage>> messages(@PathVariable String conversationId) {
        return R.ok(conversationService.listMessages(conversationId, AiSecurityUtils.requiredLoginUserId("AI 聊天")));
    }

    /**
     * 删除当前用户指定会话。
     *
     * @param conversationId 会话编号
     * @return 操作结果
     */
    @Operation(summary = "删除 AI 会话")
    @DeleteMapping("/conversations/{conversationId}")
    public R<Void> deleteConversation(@PathVariable String conversationId) {
        conversationService.deleteConversation(conversationId, AiSecurityUtils.requiredLoginUserId("AI 聊天"));
        return R.ok();
    }
}
