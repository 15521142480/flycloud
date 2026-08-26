package com.fly.ai.controller;

import com.fly.ai.model.AiEmbeddingRequest;
import com.fly.ai.model.AiEmbeddingResponse;
import com.fly.ai.service.AiEmbeddingService;
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
 * 第一版原生 Embedding 调用测试控制器。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Tag(name = "AI 原生向量测试")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/embedding")
public class AiEmbeddingController {

    private final AiEmbeddingService aiEmbeddingService;

    @Operation(summary = "文本向量化测试", description = "调用当前 provider 的原生 Embedding API，供后续 RAG 使用")
    @PostMapping
    public R<AiEmbeddingResponse> embed(@Valid @RequestBody AiEmbeddingRequest request) {
        return R.ok(aiEmbeddingService.embed(request));
    }

}
