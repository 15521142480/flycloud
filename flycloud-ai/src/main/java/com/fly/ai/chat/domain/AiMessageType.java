package com.fly.ai.chat.domain;

/**
 * 统一聊天消息类型。
 *
 * @author lxs
 * @date 2026-08-28
 */
public enum AiMessageType {

    TEXT("text"),
    TOOL_CALL("tool_call"),
    TOOL_RESULT("tool_result");

    private final String value;

    AiMessageType(String value) {
        this.value = value;
    }

    /**
     * 获取数据库存储值。
     *
     * @return 小写消息类型值
     */
    public String getValue() {
        return value;
    }
}
