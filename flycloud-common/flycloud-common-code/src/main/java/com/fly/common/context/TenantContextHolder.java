package com.fly.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * 多租户Holder
 *
 */
@UtilityClass
public class TenantContextHolder {


	/**
	 * 支持父子线程之间的数据传递
	 */
	private final ThreadLocal<String> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();


	/**
	 * 清除tenantId
	 */
	public void clear() {
		THREAD_LOCAL_TENANT.remove();
	}
}
