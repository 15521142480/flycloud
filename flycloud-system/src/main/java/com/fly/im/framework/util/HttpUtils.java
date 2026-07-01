package com.fly.im.framework.util;

/**
 * HTTP 工具。
 *
 * @author lxs
 * @date 2026-07-02
 */
public class HttpUtils {

    public static String append(String url, String name, Object value) {
        String separator = url.contains("?") ? "&" : "?";
        return url + separator + name + "=" + value;
    }

    public static String wsUrlToHttp(String url) {
        if (url == null) {
            return null;
        }
        if (url.startsWith("wss://")) {
            return "https://" + url.substring("wss://".length());
        }
        if (url.startsWith("ws://")) {
            return "http://" + url.substring("ws://".length());
        }
        return url;
    }

}
