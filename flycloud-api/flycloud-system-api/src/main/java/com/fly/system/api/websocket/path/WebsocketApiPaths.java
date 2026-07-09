package com.fly.system.api.websocket.path;

import com.fly.common.constant.CommonConstants;

/**
 * websocket请求api路径
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
