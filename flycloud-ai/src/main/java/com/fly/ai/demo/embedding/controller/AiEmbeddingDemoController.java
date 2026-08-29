package com.fly.ai.demo.embedding.controller;

import com.fly.ai.common.knowledge.model.AiEmbeddingResult;
import com.fly.ai.common.knowledge.service.AiKnowledgeService;
import com.fly.common.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第 5 步 Embedding 学习测试控制器。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Validated
@Tag(name = "AI Embedding 测试")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/demo/embedding")
public class AiEmbeddingDemoController {

    private final AiKnowledgeService knowledgeService;

    /**
     * 将输入文本发送至百炼 text-embedding-v4。
     *
     * @param text 待向量化文本
     * @return 真实向量结果
     */
    @Operation(summary = "文本向量化测试")
    @PostMapping
    public R<AiEmbeddingResult> embed(@RequestParam @NotBlank(message = "text 不能为空") String text) {
        return R.ok(knowledgeService.embed(text));
    }
}
