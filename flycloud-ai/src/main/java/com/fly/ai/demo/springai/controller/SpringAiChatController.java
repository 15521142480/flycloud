package com.fly.ai.demo.springai.controller;

import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.model.AiChatResponse;
import com.fly.ai.demo.springai.service.SpringAiChatService;
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
 * Spring AI 聊天测试控制器。
 *
 * @author lxs
 * @date 2026-08-26
 */
@Tag(name = "AI Spring AI 聊天测试")
@RestController
@RequiredArgsConstructor
@RequestMapping({ "/ai/demo/spring-ai/chat", "/ai/spring/chat" })
public class SpringAiChatController {

    private final SpringAiChatService springAiChatService;

    /**
     * 使用 Spring AI 发起一次性返回完整结果的聊天请求。
     *
     * @param request 聊天请求
     * @return 模型完整响应
     */
    @Operation(summary = "Spring AI 普通聊天测试", description = "通过 Spring AI ChatClient 调用当前配置的模型")
    @PostMapping
    public R<AiChatResponse> chat(@Valid @RequestBody AiChatRequest request) {
        return R.ok(springAiChatService.chat(request));
    }

    /**
     * 使用 Spring AI 发起以 SSE 逐段返回内容的聊天请求。
     *
     * @param request 聊天请求
     * @return SSE 响应发送器
     */
    @Operation(summary = "Spring AI 流式聊天测试", description = "通过 Spring AI ChatClient 流式调用当前配置的模型")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@Valid @RequestBody AiChatRequest request) {
        return springAiChatService.stream(request);
    }
}
