package com.fly.im.framework.tenant;

import java.lang.annotation.*;

/**
 * 忽略租户注解。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantIgnore {
}
