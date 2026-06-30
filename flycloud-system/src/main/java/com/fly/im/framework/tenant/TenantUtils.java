package com.fly.im.framework.tenant;

/**
 * 租户工具适配。
 *
 * @author lxs
 * @date 2026-06-30
 */
public class TenantUtils {

    public static void executeIgnore(Runnable runnable) {
        runnable.run();
    }

    public static <T> T execute(Long tenantId, java.util.function.Supplier<T> supplier) {
        Long oldTenantId = TenantContextHolder.getTenantId();
        try {
            TenantContextHolder.setTenantId(tenantId);
            return supplier.get();
        } finally {
            TenantContextHolder.setTenantId(oldTenantId);
        }
    }

}
