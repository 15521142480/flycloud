package com.fly.bpm.api.constants;

import com.fly.common.constant.CommonConstants;

/**
 * 工作流api常量
 *
 */
public interface BpmFeignApiConstants {


    /**
     * 远程调用公共前缀
     */
    String PROVIDER = CommonConstants.FEIGN_API_PREFIX +  "/bpm";


    /**
     * 创建流程实例，返回实例编号
     */
    String PROVIDER_INSTANCE_CREATE = PROVIDER + "/instance/create";




}
