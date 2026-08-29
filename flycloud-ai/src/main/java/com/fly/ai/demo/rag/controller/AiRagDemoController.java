package com.fly.ai.demo.rag.controller;

import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.rag.model.AiRagChatResponse;
import com.fly.ai.common.rag.service.AiRagService;
import com.fly.common.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第 7 步 RAG 学习测试控制器。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Tag(name = "AI RAG 测试")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/demo/rag")
public class AiRagDemoController {

    private final AiRagService ragService;

    /**
     * 真实执行检索、上下文注入和模型回答。
     *
     * @param request 聊天请求
     * @return 回答及 Qdrant 命中片段
     */
    @Operation(summary = "RAG 问答测试")
    @PostMapping("/chat")
    public R<AiRagChatResponse> chat(@Valid @RequestBody AiChatRequest request) {
        return R.ok(ragService.chat(request));
    }
}
