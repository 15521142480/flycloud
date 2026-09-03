package com.fly.ai.common.knowledge.service;

import com.fly.ai.common.knowledge.model.AiEmbeddingResult;
import com.fly.ai.common.knowledge.model.AiKnowledgeHit;
import com.fly.ai.common.config.AiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 统一知识向量与检索服务。
 * <p>
 * 真实调用百炼 EmbeddingModel，再通过 Spring AI VectorStore 写入和检索 Qdrant。RAG、Agent 与学习 Demo
 * 都复用此服务，不单独维护向量或测试数据。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiKnowledgeService {

    private final EmbeddingModel embeddingModel;

    private final VectorStore vectorStore;

    private final AiProperties aiProperties;

    /**
     * 调用真实 Embedding 模型生成文本向量。
     *
     * @param text 待向量化文本
     * @return 模型返回的向量
     */
    public AiEmbeddingResult embed(String text) {
        float[] vector = embeddingModel.embed(text);
        return new AiEmbeddingResult(text, vector.length, vector);
    }

    /**
     * 向 Qdrant 执行相似度检索。
     *
     * @param query 用户查询文本
     * @return 命中的知识片段
     */
    public List<AiKnowledgeHit> retrieve(String query) {
        return vectorStore.similaritySearch(SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .similarityThreshold(aiProperties.getKnowledge().getSimilarityThreshold())
                        .build())
                .stream()
                .map(document -> new AiKnowledgeHit(document.getId(), document.getText(), document.getScore(),
                        document.getMetadata()))
                .toList();
    }

    /**
     * 应用启动后向 Qdrant 写入固定 ID 的测试知识。
     * <p>
     * 固定 ID 对应 Qdrant 的 upsert 语义，重复重启不会无限重复插入。后续文件上传会替换此初始化来源，
     * 但仍复用同一个 VectorStore 和检索服务。
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeDemoKnowledge() {
        List<Document> documents = List.of(
                new Document("e1bf0e3c-716c-4d69-8bb5-58f5e7ec7101", "飞翔云商城退款规定：未发货订单可由订单创建人申请退款；已付款但未发货的订单可以退款。",
                        Map.of("knowledgeBase", "商城规则", "source", "初始化测试知识", "topic", "退款")),
                new Document("e1bf0e3c-716c-4d69-8bb5-58f5e7ec7102", "飞翔云商城退款规定：已发货订单需先完成退货物流登记，平台审核通过后原路退款。",
                        Map.of("knowledgeBase", "商城规则", "source", "初始化测试知识", "topic", "退款")),
                new Document("e1bf0e3c-716c-4d69-8bb5-58f5e7ec7103", "订单查询规则：超级管理员可查询全部订单；普通用户只能查询本人创建的订单。",
                        Map.of("knowledgeBase", "商城规则", "source", "初始化测试知识", "topic", "订单权限")),
                new Document("e1bf0e3c-716c-4d69-8bb5-58f5e7ec7104", "系统用户查询工具仅返回公共用户资料，不返回密码、手机号、邮箱等敏感信息。",
                        Map.of("knowledgeBase", "系统规则", "source", "初始化测试知识", "topic", "用户权限")));
        vectorStore.add(documents);
        log.info("AI 测试知识已写入 Qdrant，documentCount={}", documents.size());
    }
}
