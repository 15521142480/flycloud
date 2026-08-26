package com.fly.ai.model;

/**
 * 向前端推送的 SSE 事件。
 *
 * @param type 事件类型：delta、completed、error
 * @param delta 本次新增文本，仅 delta 事件存在
 * @param responseId 供应商响应 ID，仅 completed 事件存在
 * @param usage Token 用量，仅 completed 事件存在
 * @param message 错误说明，仅 error 事件存在
 */
public record AiStreamEvent(String type, String delta, String responseId, AiUsage usage, String message) {

    public static AiStreamEvent delta(String delta) {
        return new AiStreamEvent("delta", delta, null, null, null);
    }

    public static AiStreamEvent completed(String responseId, AiUsage usage) {
        return new AiStreamEvent("completed", null, responseId, usage, null);
    }

    public static AiStreamEvent error(String message) {
        return new AiStreamEvent("error", null, null, null, message);
    }

}
