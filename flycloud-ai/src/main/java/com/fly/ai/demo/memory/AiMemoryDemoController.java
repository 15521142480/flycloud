package com.fly.ai.demo.memory;

import com.fly.ai.chat.model.AiUnifiedChatRequest;
import com.fly.ai.chat.model.AiUnifiedChatResponse;
import com.fly.ai.chat.service.AiUnifiedChatService;
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
 * Chat Memory 学习测试控制器。
 * <p>
 * 该 Demo 复用正式统一聊天的会话、MySQL 历史、Redis ChatMemory 以及已实现工具，不复制业务代码。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Tag(name = "AI Chat Memory 测试")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/demo/memory")
public class AiMemoryDemoController {

    private final AiUnifiedChatService unifiedChatService;

    /**
     * 发起带 Chat Memory 的非流式学习请求。
     *
     * @param request 统一聊天请求
     * @return 聊天响应
     */
    @Operation(summary = "Chat Memory 普通聊天测试")
    @PostMapping("/chat")
    public R<AiUnifiedChatResponse> chat(@Valid @RequestBody AiUnifiedChatRequest request) {
        return R.ok(unifiedChatService.chat(request, AiSecurityUtils.requiredLoginUserId("AI Chat Memory")));
    }

    /**
     * 发起带 Chat Memory 的流式学习请求。
     *
     * @param request 统一聊天请求
     * @return SSE 发送器
     */
    @Operation(summary = "Chat Memory 流式聊天测试")
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@Valid @RequestBody AiUnifiedChatRequest request) {
        return unifiedChatService.stream(request, AiSecurityUtils.requiredLoginUserId("AI Chat Memory"));
    }
}
