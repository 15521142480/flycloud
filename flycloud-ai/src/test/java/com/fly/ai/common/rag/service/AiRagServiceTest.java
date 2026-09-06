package com.fly.ai.common.rag.service;

import com.fly.ai.common.knowledge.model.AiKnowledgeHit;
import com.fly.ai.common.knowledge.service.AiKnowledgeService;
import com.fly.ai.common.rag.model.AiRagContext;
import com.fly.ai.common.springai.SpringAiModelProviderRouter;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 验证 RAG 学习 Demo 使用共享知识服务的完整 TopK 检索结果。
 */
class AiRagServiceTest {

    @Test
    void shouldUseDemoRetrievalForLearningContext() {
        AiKnowledgeService knowledgeService = mock(AiKnowledgeService.class);
        List<AiKnowledgeHit> references = List.of(
                new AiKnowledgeHit("knowledge-1", "飞翔云商城退款规定：未发货订单可以退款。", 0.42d, Map.of()));
        when(knowledgeService.retrieveForDemo("退款规定")).thenReturn(references);
        AiRagService service = new AiRagService(knowledgeService, mock(SpringAiModelProviderRouter.class));

        AiRagContext context = service.retrieveContextForDemo("退款规定");

        assertEquals(references, context.references());
        assertTrue(context.systemPrompt().contains("未发货订单可以退款"));
        verify(knowledgeService).retrieveForDemo("退款规定");
    }
}
