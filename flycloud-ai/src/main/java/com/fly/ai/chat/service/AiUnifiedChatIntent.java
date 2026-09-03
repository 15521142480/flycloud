package com.fly.ai.chat.service;

/**
 * 正式统一聊天本轮需要启用的能力。
 *
 * @author lxs
 * @date 2026-09-03
 */
public enum AiUnifiedChatIntent {

    /** 纯模型通用问答，仅保留会话记忆。 */
    GENERAL(false, false),

    /** 业务资源查询，允许模型按需调用受控工具。 */
    BUSINESS_TOOL(true, false),

    /** 公司知识规则问答，启用相关度受限的 RAG。 */
    KNOWLEDGE_RAG(false, true),

    /** 需要业务事实和公司规则的复合任务。 */
    BUSINESS_WITH_KNOWLEDGE(true, true);

    private final boolean toolCallingEnabled;

    private final boolean ragEnabled;

    AiUnifiedChatIntent(boolean toolCallingEnabled, boolean ragEnabled) {
        this.toolCallingEnabled = toolCallingEnabled;
        this.ragEnabled = ragEnabled;
    }

    /**
     * 当前轮是否允许挂载受控业务工具。
     *
     * @return 是否启用 Tool Calling
     */
    public boolean isToolCallingEnabled() {
        return toolCallingEnabled;
    }

    /**
     * 当前轮是否允许检索内部知识库。
     *
     * @return 是否启用 RAG
     */
    public boolean isRagEnabled() {
        return ragEnabled;
    }
}
