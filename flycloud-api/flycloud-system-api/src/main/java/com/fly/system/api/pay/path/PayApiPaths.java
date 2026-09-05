package com.fly.system.api.pay.path;

import com.fly.common.constant.CommonConstants;

/**
 * 支付服务内部接口路径常量。
 *
 * @author lxs
 */
public interface PayApiPaths {

    /**
     * 支付服务远程调用公共前缀。
     */
    String PROVIDER = CommonConstants.FEIGN_API_PREFIX + "/sys/pay";

    /**
     * 创建支付订单。
     */
    String PROVIDER_ORDER_CREATE = PROVIDER + "/order/create";

    /**
     * 查询支付订单。
     */
    String PROVIDER_ORDER_GET = PROVIDER + "/order/get";

}
