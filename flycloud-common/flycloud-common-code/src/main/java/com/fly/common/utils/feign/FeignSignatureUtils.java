package com.fly.common.utils.feign;

import cn.hutool.core.util.StrUtil;
import com.fly.common.constant.CommonConstants;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Locale;

/**
 * Feign内部接口签名工具。
 *
 * @author lxs
 * @date 2026/7/9
 */
public final class FeignSignatureUtils {

    public static final String HEADER_TIMESTAMP = "X-Feign-Timestamp";

    public static final String HEADER_NONCE = "X-Feign-Nonce";

    public static final String HEADER_SIGNATURE = "X-Feign-Signature";

    private static final String HMAC_SHA256 = "HmacSHA256";

    private FeignSignatureUtils() {
    }

    /**
     * 判断是否为Feign内部接口路径。
     *
     * @param path 请求路径
     * @return 是否为Feign内部接口
     */
    public static boolean isFeignPath(String path) {
        return normalizePath(path).startsWith(CommonConstants.FEIGN_API_PREFIX + "/")
                || CommonConstants.FEIGN_API_PREFIX.equals(normalizePath(path));
    }

    /**
     * 生成Feign内部接口签名。
     *
     * @param secret 密钥
     * @param method 请求方法
     * @param path 请求路径
     * @param timestamp 时间戳
     * @param nonce 随机串
     * @return 签名值
     */
    public static String sign(String secret, String method, String path, String timestamp, String nonce) {
        if (StrUtil.hasBlank(secret, method, path, timestamp, nonce)) {
            return "";
        }
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
            byte[] bytes = mac.doFinal(buildSignText(method, path, timestamp, nonce).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (Exception ex) {
            throw new IllegalStateException("生成Feign内部接口签名失败", ex);
        }
    }

    /**
     * 校验Feign内部接口签名。
     *
     * @param secret 密钥
     * @param method 请求方法
     * @param path 请求路径
     * @param timestamp 时间戳
     * @param nonce 随机串
     * @param signature 待校验签名
     * @param expireSeconds 时间戳有效期
     * @return 是否校验通过
     */
    public static boolean verify(String secret, String method, String path, String timestamp, String nonce,
                                 String signature, long expireSeconds) {
        if (StrUtil.hasBlank(secret, method, path, timestamp, nonce, signature)) {
            return false;
        }
        if (!isTimestampValid(timestamp, expireSeconds)) {
            return false;
        }
        String actual = sign(secret, method, path, timestamp, nonce);
        return MessageDigest.isEqual(
                actual.getBytes(StandardCharsets.UTF_8),
                signature.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 规范化请求路径，去除查询参数并保证以斜杠开头。
     *
     * @param path 请求路径
     * @return 规范化后的路径
     */
    public static String normalizePath(String path) {
        if (StrUtil.isBlank(path)) {
            return "/";
        }
        String normalized = StrUtil.subBefore(path, "?", false);
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        return normalized;
    }

    private static String buildSignText(String method, String path, String timestamp, String nonce) {
        return method.toUpperCase(Locale.ROOT)
                + "\n" + normalizePath(path)
                + "\n" + timestamp
                + "\n" + nonce;
    }

    private static boolean isTimestampValid(String timestamp, long expireSeconds) {
        try {
            long requestTime = Long.parseLong(timestamp);
            long now = Instant.now().getEpochSecond();
            return Math.abs(now - requestTime) <= expireSeconds;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
