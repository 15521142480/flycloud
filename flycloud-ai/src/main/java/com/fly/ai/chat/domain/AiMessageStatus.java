package com.fly.ai.chat.domain;

/**
 * 模型消息处理状态。
 *
 * @author lxs
 * @date 2026-08-28
 */
public enum AiMessageStatus {

    COMPLETED("completed"),
    FAILED("failed");

    private final String value;

    AiMessageStatus(String value) {
        this.value = value;
    }

    /**
     * 获取数据库存储值。
     *
     * @return 小写状态值
     */
    public String getValue() {
        return value;
    }
}
