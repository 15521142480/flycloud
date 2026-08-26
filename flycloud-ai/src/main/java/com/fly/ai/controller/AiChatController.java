package com.fly.ai.controller;

import com.fly.ai.model.AiChatRequest;
import com.fly.ai.model.AiChatResponse;
import com.fly.ai.service.AiChatService;
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
 * 第一版原生模型调用测试控制器。
 * <p>
 * 此控制器不依赖供应商 SDK，调用链为 Controller -> Service -> Provider Router -> JDK HttpClient。
 * 可直接通过 Swagger 或 Postman 验证普通聊天和 SSE 流式响应。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Tag(name = "AI 原生聊天测试")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/chat")
public class AiChatController {

    private final AiChatService aiChatService;

    @Operation(summary = "普通聊天测试", description = "使用当前 provider，通过 JDK HttpClient 发起一次真实模型请求")
    @PostMapping
    public R<AiChatResponse> chat(@Valid @RequestBody AiChatRequest request) {
        return R.ok(aiChatService.chat(request));
    }

    @Operation(summary = "流式聊天测试", description = "使用当前 provider，通过 SSE 逐段返回真实模型输出")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@Valid @RequestBody AiChatRequest request) {
        return aiChatService.stream(request);
    }

}
