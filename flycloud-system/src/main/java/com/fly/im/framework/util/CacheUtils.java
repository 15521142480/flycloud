package com.fly.im.framework.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * 缓存工具。
 *
 * @author lxs
 * @date 2026-06-30
 */
public class CacheUtils {

    public static <T> void refresh(Collection<T> list, Consumer<T> consumer) {
        if (list == null) {
            return;
        }
        list.forEach(consumer);
    }

    public static <K, V> LoadingCache<K, V> buildAsyncReloadingCache(Duration duration, CacheLoader<K, V> loader) {
        return CacheBuilder.newBuilder()
                .refreshAfterWrite(duration)
                .build(loader);
    }

}
