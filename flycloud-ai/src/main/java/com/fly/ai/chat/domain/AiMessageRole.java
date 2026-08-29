package com.fly.ai.chat.domain;

/**
 * 统一聊天消息角色。
 *
 * @author lxs
 * @date 2026-08-28
 */
public enum AiMessageRole {

    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system"),
    TOOL("tool");

    private final String value;

    AiMessageRole(String value) {
        this.value = value;
    }

    /**
     * 获取数据库存储值。
     *
     * @return 小写角色值
     */
    public String getValue() {
        return value;
    }
}
