package com.fly.ai.chat.service;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 正式统一聊天的轻量意图路由器。
 * <p>
 * 该路由只决定本轮是否暴露已有能力，不负责理解或生成回答，因此不会额外消耗一次模型调用。涉及订单、用户
 * 等受保护资源时才启用 Tool Calling；涉及公司规则时才尝试 RAG，普通问题直接交给模型自身知识回答。
 *
 * @author lxs
 * @date 2026-09-03
 */
@Component
public class AiUnifiedChatIntentRouter {

    private static final Pattern BUSINESS_RESOURCE_PATTERN = Pattern.compile(
            "(?is)(?:查询|查看|获取|找|检索).{0,16}(?:订单|订单号|流水号|用户|用户信息|用户资料)"
                    + "|(?:这个|该|此)(?:订单|用户)"
                    + "|订单.{0,16}(?:谁买|谁购买|购买者|买家|详情|信息|状态)"
                    + "|用户\\s*(?:ID|id|编号)"
    );

    private static final Pattern KNOWLEDGE_RULE_PATTERN = Pattern.compile(
            "(?is)(?:退款|退货|售后)"
                    + "|(?:公司|飞翔云|商城).{0,12}(?:规定|规则|制度|政策|流程)"
                    + "|(?:规定|规则|制度|政策|流程).{0,12}(?:公司|飞翔云|商城)"
    );

    /**
     * 根据用户文本选择本轮可用能力。
     *
     * @param message 用户消息
     * @return 统一聊天意图
     */
    public AiUnifiedChatIntent route(String message) {
        boolean needBusinessTools = BUSINESS_RESOURCE_PATTERN.matcher(message).find();
        boolean needKnowledge = KNOWLEDGE_RULE_PATTERN.matcher(message).find();
        if (needBusinessTools && needKnowledge) {
            return AiUnifiedChatIntent.BUSINESS_WITH_KNOWLEDGE;
        }
        if (needBusinessTools) {
            return AiUnifiedChatIntent.BUSINESS_TOOL;
        }
        if (needKnowledge) {
            return AiUnifiedChatIntent.KNOWLEDGE_RAG;
        }
        return AiUnifiedChatIntent.GENERAL;
    }
}
