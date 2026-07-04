package com.fly.system.api.constants;

import com.fly.common.constant.CommonConstants;

/**
 * member用户常量
 *
 */
public interface MemberFeignApiConstants {


    /**
     * 远程调用公共前缀
     */
    String PROVIDER = CommonConstants.FEIGN_API_PREFIX +  "/member";


    // ================================================== 用户
    /**
     * 根据id查询用户信息
     */
    String PROVIDER_MEMBER_USER_ID = PROVIDER + "/admin/member/user/id";

    /**
     * 根据手机号查询用户信息
     */
    String PROVIDER_MEMBER_USER_MOBILE = PROVIDER + "/admin/member/user/mobile";

    /**
     * 根据username查询用户信息
     */
    String PROVIDER_MEMBER_USER_IDS = PROVIDER + "/admin/member/user/ids";

    /**
     * 根据username查询用户信息
     */
    String PROVIDER_MEMBER_USER_USERNAME = PROVIDER + "/admin/member/user/username";


    // 根据ids验证用户
    String PROVIDER_MEMBER_USER_VALID_IDS = PROVIDER + "/admin/member/user/valid/ids";






}
