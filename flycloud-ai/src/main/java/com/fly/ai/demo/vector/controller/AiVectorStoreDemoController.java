package com.fly.ai.demo.vector.controller;

import com.fly.ai.common.knowledge.model.AiKnowledgeHit;
import com.fly.ai.common.knowledge.service.AiKnowledgeService;
import com.fly.common.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 第 6 步 Qdrant Vector Store 学习测试控制器。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Validated
@Tag(name = "AI Vector Store 测试")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/demo/vector")
public class AiVectorStoreDemoController {

    private final AiKnowledgeService knowledgeService;

    /**
     * 以真实向量检索 Qdrant 初始化知识。
     *
     * @param query 查询文本
     * @return 相似知识片段
     */
    @Operation(summary = "Qdrant 相似度检索测试")
    @GetMapping("/search")
    public R<List<AiKnowledgeHit>> search(@RequestParam @NotBlank(message = "query 不能为空") String query) {
        return R.ok(knowledgeService.retrieve(query));
    }
}
