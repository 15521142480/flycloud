package com.fly.ai.client;

import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpRequest;

/**
 * AI 模型 HTTP 调用日志。
 * <p>
 * 仅记录请求方法、地址和请求体，绝不记录 Authorization 等认证头；响应内容只保留前 50 个字符，
 * 以兼顾问题排查、日志成本与敏感凭据保护。
 *
 * @author lxs
 * @date 2026-08-26
 */
@Slf4j
final class AiHttpLog {

    private static final int RESPONSE_PREVIEW_LENGTH = 50;

    /**
     * 禁止创建工具类实例。
     */
    private AiHttpLog() {
    }

    /**
     * 记录模型 HTTP 请求，不记录认证头。
     *
     * @param providerName 供应商名称
     * @param request HTTP 请求
     * @param requestBody 请求 JSON
     */
    static void request(String providerName, HttpRequest request, String requestBody) {
        log.info("AI 模型请求，provider={}, method={}, url={}, body={}", providerName, request.method(), request.uri(), requestBody);
    }

    /**
     * 记录模型 HTTP 响应，响应内容仅保留前 50 个字符。
     *
     * @param providerName 供应商名称
     * @param request HTTP 请求
     * @param statusCode HTTP 状态码
     * @param responseBody 响应内容
     */
    static void response(String providerName, HttpRequest request, int statusCode, String responseBody) {
        log.info("AI 模型响应，provider={}, method={}, url={}, statusCode={}, bodyPreview={}",
                providerName, request.method(), request.uri(), statusCode, preview(responseBody));
    }

    /**
     * 截取适用于日志记录的响应内容预览。
     *
     * @param content 原始响应内容
     * @return 最多包含前 50 个字符的内容预览
     */
    static String preview(CharSequence content) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        if (content.length() <= RESPONSE_PREVIEW_LENGTH) {
            return content.toString();
        }
        return content.subSequence(0, RESPONSE_PREVIEW_LENGTH) + "...";
    }
}
