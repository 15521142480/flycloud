package com.fly.ai.chat.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link AiUnifiedChatIntentRouter} 单元测试。
 */
class AiUnifiedChatIntentRouterTest {

    private final AiUnifiedChatIntentRouter router = new AiUnifiedChatIntentRouter();

    /** 通用问题不应暴露工具或检索内部知识。 */
    @Test
    void shouldRouteGeneralQuestionWithoutToolsOrRag() {
        assertEquals(AiUnifiedChatIntent.GENERAL, router.route("如何看待 AI 各平台的智能程度和 Token 收费标准？"));
    }

    /** 订单查询应进入受控工具调用。 */
    @Test
    void shouldRouteOrderQueryToBusinessTools() {
        assertEquals(AiUnifiedChatIntent.BUSINESS_TOOL, router.route("查询订单 ID 2073133434168320001 的信息"));
    }

    /** 公司规则问题只启用 RAG。 */
    @Test
    void shouldRouteCompanyRuleToRag() {
        assertEquals(AiUnifiedChatIntent.KNOWLEDGE_RAG, router.route("公司的退款规定是什么？"));
    }

    /** 查询订单并判断退款时应组合工具与 RAG。 */
    @Test
    void shouldRouteCompositeQuestionToToolsAndRag() {
        assertEquals(AiUnifiedChatIntent.BUSINESS_WITH_KNOWLEDGE,
                router.route("查询订单 ID 2073133434168320001，并根据公司退款规则判断能否退款"));
    }
}
