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

    String MOBILE = "mobile";

    /**
     * 图文点选验证码
     */
    String CHAR_POOL = "春夏秋冬东南西北山河湖海风雨星月天地云竹松梅兰书画茶光影晨暮苹果香蕉西瓜葡萄橘子草莓桃子梨子樱桃柠檬小猫小狗老虎狮子大象猴子兔子熊猫狐狸斑马红蓝绿黄黑白紫粉灰橙色太阳月亮星星云朵雨伞雪花火焰河流大山树木汽车飞机火车轮船手机电脑书包铅笔椅子桌子";
    String CHALLENGE_KEY_PREFIX = "captcha:click:challenge:";
    String VERIFIED_KEY_PREFIX = "captcha:click:verified:";
    String DATA_URL_PREFIX = "data:image/png;base64,";
    int IMAGE_WIDTH = 360;
    int IMAGE_HEIGHT = 240;
    int TARGET_COUNT = 3;
    int DISTRACTOR_COUNT = 0;

    int CAPTCHA_CHALLENGE_TTL_SECONDS = 120;
    int CAPTCHA_VERIFICATION_TOKEN_TTL_SECONDS = 120;
    int CAPTCHA_TOLERANCE_PIXELS = 28;


}
