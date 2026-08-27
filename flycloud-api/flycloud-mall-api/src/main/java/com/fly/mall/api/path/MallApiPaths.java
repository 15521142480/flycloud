package com.fly.mall.api.path;

import com.fly.common.constant.CommonConstants;

/**
 * 商城服务 Feign 常量。
 *
 * @author lxs
 * @date 2026-07-02
 */
public class MallApiPaths {

    /**
     * 商城服务名称。
     */
    public static final String SERVER_NAME = "flycloud-mall";

    /**
     * 商城服务 Feign 内部接口前缀。
     */
    public static final String PROVIDER = CommonConstants.FEIGN_API_PREFIX + "/mall";

    /**
     * 根据订单编号查询订单。
     */
    public static final String PROVIDER_TRADE_ORDER_ID = PROVIDER + "/trade/order/id";

}
