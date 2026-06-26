package com.fly.common.constant;

/**
 * 权限常量
 *
 * @author: lxs
 * @date: 2026/6/26
 */
public interface AuthConstants {


    String GRANT_TYPE_CAPTCHA = "captcha";

    String GRANT_TYPE_SMS = "sms";

    String GRANT_TYPE_SOCIAL = "social";

    String ACCESS_TOKEN_KEY = "fly.oauth.access.";

    String GATEWAY_ACCESS_TOKEN_KEY = "auth:";

    String REFRESH_TOKEN_KEY = "fly.oauth.refresh.";

    String SOCIAL_STATE_PREFIX = "SOCIAL::STATE::";

    String AUTHORITIES = "authorities";

    String DEPT_ID = "deptId";

    String PHONE = "phone";
}
