package com.fly.ai.common.rag.service;

import com.fly.ai.common.knowledge.model.AiKnowledgeHit;
import com.fly.ai.common.knowledge.service.AiKnowledgeService;
import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.model.AiChatResponse;
import com.fly.ai.common.rag.model.AiRagChatResponse;
import com.fly.ai.common.rag.model.AiRagContext;
import com.fly.ai.common.springai.SpringAiModelProviderRouter;
import com.fly.ai.common.utils.SpringAiChatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 统一 RAG 服务。
 * <p>
 * 先在 Qdrant 真实检索，再把命中的片段作为受约束上下文交给当前 ChatClient。Agent 与正式统一聊天复用
 * {@link #retrieveContext(String)}，不复制检索逻辑。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Service
@RequiredArgsConstructor
public class AiRagService {

    private final AiKnowledgeService knowledgeService;

    private final SpringAiModelProviderRouter providerRouter;

    /**
     * 执行完整的检索增强问答。
     *
     * @param request 聊天请求
     * @return 回答及引用片段
     */
    public AiRagChatResponse chat(AiChatRequest request) {
        AiRagContext context = retrieveContext(request.message());
        SpringAiModelProviderRouter.SelectedChatClient selected = providerRouter.getSelectedChatClient();
        ChatResponse response = SpringAiChatUtils.requestSpec(selected.chatClient(), request)
                .system(context.systemPrompt())
                .call()
                .chatResponse();
        return new AiRagChatResponse(SpringAiChatUtils.toChatResponse(response), context.references());
    }

    /**
     * 获取供统一聊天或 Agent 注入的 RAG 上下文。
     *
     * @param query 用户问题
     * @return 真实检索片段格式化后的上下文；无命中时为空
     */
    public AiRagContext retrieveContext(String query) {
        List<AiKnowledgeHit> references = knowledgeService.retrieve(query);
        return new AiRagContext(references.isEmpty() ? "" : ragSystemPrompt(references), references);
    }

    /**
     * 将检索结果转换为模型上下文，明确要求不能编造未命中的规则。
     *
     * @param references 知识片段
     * @return 系统提示词补充
     */
    private String ragSystemPrompt(List<AiKnowledgeHit> references) {
        StringBuilder context = new StringBuilder("以下是从飞翔云知识库检索到的参考资料。仅可依据这些资料回答相关规则；资料不足时明确说明。\n");
        for (int index = 0; index < references.size(); index++) {
            AiKnowledgeHit reference = references.get(index);
            context.append('[').append(index + 1).append("] ").append(reference.content()).append('\n');
        }
        return context.toString();
    }
}
