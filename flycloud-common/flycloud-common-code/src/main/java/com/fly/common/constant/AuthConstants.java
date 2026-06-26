package com.fly.common.constant;

import java.time.Duration;

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

    Duration ACCESS_TOKEN_TTL = Duration.ofHours(2);

    Duration REFRESH_TOKEN_TTL = Duration.ofDays(30);
}
