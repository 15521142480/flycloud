package com.fly.common.utils.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fly.common.exception.AiProviderException;

import java.net.URI;

/**
 * AI 模块公共工具。
 * <p>
 * 仅放置原生调用与 Spring AI 调用均可复用、且不包含供应商业务规则的基础操作。
 *
 * @author lxs
 * @date 2026-08-26
 */
public final class AiUtils {

    /** 模型输出日志预览的最大字符数。 */
    public static final int MODEL_CONTENT_PREVIEW_LENGTH = 50;

    private AiUtils() {
    }

    /**
     * 判断字符串是否包含非空白字符。
     *
     * @param value 待判断字符串
     * @return 存在有效内容时返回 {@code true}
     */
    public static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * 校验 AI 服务总开关是否已开启。
     *
     * @param enabled AI 服务开关
     */
    public static void requireServiceEnabled(boolean enabled) {
        if (!enabled) {
            throw new AiProviderException(503, "AI 服务当前已关闭");
        }
    }

    /**
     * 截取模型文本以供日志预览。
     *
     * @param content 模型文本
     * @return 最多 {@link #MODEL_CONTENT_PREVIEW_LENGTH} 个字符的预览文本
     */
    public static String previewModelContent(String content) {
        if (content == null || content.length() <= MODEL_CONTENT_PREVIEW_LENGTH) {
            return content;
        }
        return content.substring(0, MODEL_CONTENT_PREVIEW_LENGTH) + "...";
    }

    /**
     * 将模型文本追加到流式日志预览缓冲区。
     *
     * @param contentPreview 文本预览缓冲区
     * @param content 模型文本
     */
    public static void appendModelContentPreview(StringBuilder contentPreview, String content) {
        if (content == null || contentPreview.length() >= MODEL_CONTENT_PREVIEW_LENGTH) {
            return;
        }
        contentPreview.append(content, 0, Math.min(MODEL_CONTENT_PREVIEW_LENGTH - contentPreview.length(), content.length()));
    }

    /**
     * 将可空整数转换为零值长整型。
     *
     * @param value 可空整数
     * @return 非空数值或零
     */
    public static long valueOrZero(Integer value) {
        return value == null ? 0L : value.longValue();
    }

    /**
     * 拼接服务地址和 API 路径。
     *
     * @param baseUrl 服务地址
     * @param path API 路径
     * @return 完整请求 URI
     */
    public static URI resolveUri(String baseUrl, String path) {
        String normalizedBaseUrl = baseUrl.replaceAll("/+$", "");
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        return URI.create(normalizedBaseUrl + normalizedPath);
    }

    /**
     * 将 AI 请求 JSON 对象序列化为字符串。
     *
     * @param objectMapper JSON 序列化工具
     * @param body 请求 JSON 对象
     * @return JSON 字符串
     */
    public static String toJson(ObjectMapper objectMapper, ObjectNode body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (Exception exception) {
            throw new AiProviderException(500, "AI 请求 JSON 序列化失败", exception);
        }
    }

    /**
     * 提取可安全返回给客户端的流式调用失败说明。
     *
     * @param exception 异常对象
     * @param fallbackMessage 非供应商异常时的兜底说明
     * @return 错误说明
     */
    public static String providerMessage(Exception exception, String fallbackMessage) {
        if (exception instanceof AiProviderException providerException) {
            return providerException.getMessage();
        }
        return fallbackMessage;
    }
}
