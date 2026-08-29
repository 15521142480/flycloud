package com.fly.ai.demo.tool.controller;

import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.utils.AiSecurityUtils;
import com.fly.ai.common.tool.model.AiToolCallingResponse;
import com.fly.ai.common.tool.service.AiToolCallingChatService;
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
 * AI Tool Calling 测试控制器。
 *
 * @author lxs
 * @date 2026-08-27
 */
@Tag(name = "AI Tool Calling 测试")
@RestController
@RequiredArgsConstructor
@RequestMapping({ "/ai/demo/tool", "/ai/tool" })
public class AiToolCallingController {

    private final AiToolCallingChatService aiToolCallingChatService;

    /**
     * 发起可调用系统用户和商城订单工具的聊天请求。
     * <p>
     * 本接口由统一 Spring Security 认证保护；当前登录用户只从服务端安全上下文读取。
     *
     * @param request 聊天请求
     * @return Tool Calling 聊天响应
     */
    @Operation(summary = "Tool Calling 聊天测试", description = "模型可调用系统用户查询和商城订单查询工具，订单查询会在服务端二次校验资源权限")
    @PostMapping("/chat")
    public R<AiToolCallingResponse> chat(@Valid @RequestBody AiChatRequest request) {
        return R.ok(aiToolCallingChatService.chat(request, AiSecurityUtils.requiredLoginUserId("AI Tool Calling")));
    }

    /**
     * 发起流式 Tool Calling 聊天请求。
     * <p>
     * 模型可先调用受控业务工具，再通过 SSE 逐段输出最终回答。订单工具的资源权限判断始终在服务端执行。
     *
     * @param request 聊天请求
     * @return SSE 响应发送器
     */
    @Operation(summary = "Tool Calling 流式聊天测试", description = "模型在流式调用中执行受控工具；无订单权限时仅返回固定拒绝提示")
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@Valid @RequestBody AiChatRequest request) {
        return aiToolCallingChatService.stream(request, AiSecurityUtils.requiredLoginUserId("AI Tool Calling"));
    }
}
