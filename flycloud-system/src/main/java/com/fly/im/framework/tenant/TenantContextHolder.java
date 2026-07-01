package com.fly.im.framework.tenant;

/**
 * 租户上下文适配。
 *
 * @author lxs
 * @date 2026-07-02
 */
public class TenantContextHolder {

    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();

    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    public static void setTenantId(Long tenantId) {
        if (tenantId == null) {
            TENANT_ID.remove();
            return;
        }
        TENANT_ID.set(tenantId);
    }

    public static void setIgnore(Boolean ignore) {
    }

    public static Long getRequiredTenantId() {
        Long tenantId = getTenantId();
        return tenantId != null ? tenantId : 0L;
    }

}
