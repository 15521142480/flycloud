package com.fly.pay.utils;

import com.fly.common.utils.StringUtils;
import com.fly.common.utils.json.JsonUtils;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 支付回调数据解析工具。
 *
 * @author lxs
 * @date 2026-07-01
 */
public final class PayNotifyParseUtils {

    private PayNotifyParseUtils() {
    }

    /**
     * 合并 URL 参数和 JSON 请求体，方便不同支付渠道共用一套基础解析。
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseNotifyData(Map<String, String> params, String body) {
        Map<String, String> data = new LinkedHashMap<>();
        if (params != null) {
            data.putAll(params);
        }
        if (StringUtils.isNotBlank(body)) {
            try {
                Map<String, Object> bodyMap = JsonUtils.parseObject(body, Map.class);
                if (bodyMap != null) {
                    bodyMap.forEach((key, value) -> {
                        if (value != null) {
                            data.put(key, String.valueOf(value));
                        }
                    });
                }
            } catch (RuntimeException ignored) {
                data.put("body", body);
            }
        }
        return data;
    }

    /**
     * 按候选字段顺序读取第一个有值的字段。
     */
    public static String firstValue(Map<String, String> data, String... keys) {
        if (data == null || keys == null) {
            return null;
        }
        for (String key : keys) {
            String value = data.get(key);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 记录原始通知数据。
     */
    public static String toNotifyData(Map<String, String> params, String body, Map<String, String> headers) {
        Map<String, Object> notifyData = new LinkedHashMap<>();
        notifyData.put("params", params);
        notifyData.put("body", body);
        notifyData.put("headers", headers);
        return JsonUtils.toJsonString(notifyData);
    }
}
