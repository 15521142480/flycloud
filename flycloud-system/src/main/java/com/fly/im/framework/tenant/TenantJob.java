package com.fly.im.framework.tenant;

import java.lang.annotation.*;

/**
 * 租户任务注解适配。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantJob {
}
