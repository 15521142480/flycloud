package com.fly.ai.common.knowledge.service;

import com.fly.ai.common.config.AiProperties;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 验证正式 RAG 与学习 Demo 共用检索服务但使用不同的结果过滤策略。
 */
class AiKnowledgeServiceTest {

    @Test
    void shouldApplyConfiguredThresholdForOfficialRetrieval() {
        AiProperties properties = new AiProperties();
        properties.getKnowledge().setSimilarityThreshold(0.7d);
        VectorStore vectorStore = vectorStoreWithResult();
        AiKnowledgeService service = new AiKnowledgeService(mock(EmbeddingModel.class), vectorStore, properties);

        service.retrieve("退款规定");

        assertEquals(0.7d, searchRequest(vectorStore).getSimilarityThreshold());
    }

    @Test
    void shouldAcceptAllTopKResultsForLearningDemo() {
        VectorStore vectorStore = vectorStoreWithResult();
        AiKnowledgeService service = new AiKnowledgeService(mock(EmbeddingModel.class), vectorStore, new AiProperties());

        service.retrieveForDemo("退款规定");

        assertEquals(SearchRequest.SIMILARITY_THRESHOLD_ACCEPT_ALL,
                searchRequest(vectorStore).getSimilarityThreshold());
    }

    private VectorStore vectorStoreWithResult() {
        VectorStore vectorStore = mock(VectorStore.class);
        Document document = mock(Document.class);
        when(document.getId()).thenReturn("knowledge-1");
        when(document.getText()).thenReturn("退款规定");
        when(document.getScore()).thenReturn(0.42d);
        when(document.getMetadata()).thenReturn(java.util.Map.of());
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of(document));
        return vectorStore;
    }

    private SearchRequest searchRequest(VectorStore vectorStore) {
        ArgumentCaptor<SearchRequest> requestCaptor = ArgumentCaptor.forClass(SearchRequest.class);
        verify(vectorStore).similaritySearch(requestCaptor.capture());
        return requestCaptor.getValue();
    }
}
