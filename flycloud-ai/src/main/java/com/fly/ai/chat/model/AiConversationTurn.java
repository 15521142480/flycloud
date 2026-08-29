package com.fly.ai.chat.model;

/**
 * 一次统一聊天回合创建后的持久化标识。
 *
 * @param conversationId 本次所属会话编号
 * @param assistantMessageId 已创建的生成中助手消息编号
 * @author lxs
 * @date 2026-08-28
 */
public record AiConversationTurn(String conversationId, String assistantMessageId) {
}
