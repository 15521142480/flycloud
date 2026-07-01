package com.fly.im.framework.util;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Map 工具。
 *
 * @author lxs
 * @date 2026-07-02
 */
public class MapUtils {

    public static <K, V> void findAndThen(Map<K, V> map, K key, Consumer<V> consumer) {
        if (map == null || !map.containsKey(key)) {
            return;
        }
        consumer.accept(map.get(key));
    }

}
