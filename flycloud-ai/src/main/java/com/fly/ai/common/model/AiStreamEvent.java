package com.fly.ai.common.model;

/**
 * 向前端推送的 SSE 事件。
 *
 * @param type 事件类型：conversation、delta、permission、completed、error
 * @param delta 本次新增文本，仅 delta 事件存在
 * @param responseId 供应商响应 ID，仅 completed 事件存在
 * @param usage Token 用量，仅 completed 事件存在
 * @param message 错误说明，仅 error 事件存在
 * @param permission 工具调用权限结果，仅 permission 事件存在
 * @param conversationId 会话编号，仅 conversation 事件存在
 */
public record AiStreamEvent(String type, String delta, String responseId, AiUsage usage, String message,
        AiPermission permission, String conversationId) {

    /**
     * 创建文本增量事件。
     *
     * @param delta 本次新增文本
     * @return 增量事件
     */
    public static AiStreamEvent delta(String delta) {
        return new AiStreamEvent("delta", delta, null, null, null, null, null);
    }

    /**
     * 创建流式响应完成事件。
     *
     * @param responseId 供应商响应 ID
     * @param usage Token 用量
     * @return 完成事件
     */
    public static AiStreamEvent completed(String responseId, AiUsage usage) {
        return new AiStreamEvent("completed", null, responseId, usage, null, null, null);
    }

    /**
     * 创建流式响应错误事件。
     *
     * @param message 错误说明
     * @return 错误事件
     */
    public static AiStreamEvent error(String message) {
        return new AiStreamEvent("error", null, null, null, message, null, null);
    }

    /**
     * 创建工具调用权限事件。
     *
     * @param permission 资源权限结果
     * @return 权限事件
     */
    public static AiStreamEvent permission(AiPermission permission) {
        return new AiStreamEvent("permission", null, null, null, null, permission, null);
    }

    /**
     * 创建会话初始化事件。
     *
     * @param conversationId 服务端确认或新建的会话编号
     * @return 会话事件
     */
    public static AiStreamEvent conversation(String conversationId) {
        return new AiStreamEvent("conversation", null, null, null, null, null, conversationId);
    }

}
