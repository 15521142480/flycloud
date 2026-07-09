package com.fly.system.api.websocket.path;

import com.fly.common.constant.CommonConstants;

/**
 * member用户常量
 *
 */
public interface WebsocketApiPaths {


    /**
     * 远程调用公共前缀
     */
    String PROVIDER = CommonConstants.FEIGN_API_PREFIX +  "/websocket";


    // ==================================================

    String PROVIDER_WEBSOCKET_SEND_API = PROVIDER + "/provider/sys/send";












}
