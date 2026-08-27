package com.fly.ai.original.controller;

import com.fly.ai.common.model.AiEmbeddingRequest;
import com.fly.ai.common.model.AiEmbeddingResponse;
import com.fly.ai.original.service.AiEmbeddingService;
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
@RequestMapping({ "/ai/original/embedding", "/ai/embedding" })
public class AiEmbeddingController {

    private final AiEmbeddingService aiEmbeddingService;

    /**
     * 将输入文本转换为向量。
     *
     * 也就是把文本转成数字；比如：
     * 【我想买一台苹果手机】的向量数据是：[0.81, 0.13, -0.52, 0.37, ...]
     * 【推荐一款 iPhone 给我】的向量数据是：[0.79, 0.15, -0.48, 0.40, ...]
     *
     * 然后计算两个向量之间的距离/相似度，比如相似度：0.94
     *
     * @param request 向量化请求
     * @return 模型返回的文本向量
     */
    @Operation(summary = "文本向量化测试", description = "调用当前 provider 的原生 Embedding API，供后续 RAG 使用")
    @PostMapping
    public R<AiEmbeddingResponse> embed(@Valid @RequestBody AiEmbeddingRequest request) {
        return R.ok(aiEmbeddingService.embed(request));
    }

}
