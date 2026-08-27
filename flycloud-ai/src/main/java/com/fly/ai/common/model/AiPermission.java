package com.fly.ai.common.model;

/**
 * AI 工具调用的资源权限结果。
 *
 * @param granted 是否拥有资源访问权限
 * @param message 面向用户展示的权限说明
 */
public record AiPermission(boolean granted, String message) {
}
