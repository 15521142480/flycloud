

package com.fly.common.constant;

/**
 * @author lxs
 */
public interface CommonConstants {


    // todo 请求相应相关
    // 成功标记
    Integer SUCCESS = 0;

    // 失败标记
    Integer FAIL = 1;

    // 请求超时时间 默认为二分钟
    Integer HTTP_TIMEOUT_MILLION_SECONDS = 120 * 1000;


    // todo 授权相关
    // 请求头 header
    String HEADER = "header";

    // 请求头-授权key
    String AUTHORIZATION_KEY = "Authorization";

    // 请求头-授权-前缀 Bearer
    String AUTHORIZATION_BEARER = "Bearer"; // bearer
    String AUTHORIZATION_BEARER_HAS_BLANK = "Bearer "; // bearer

    // 请求头-授权-前缀 Basic
    String AUTHORIZATION_BASIC = "Basic";
    String AUTHORIZATION_BASIC_HAS_BLANK = "Basic ";

    // 移动端接口请求头
    String AUTHORIZATION_MOBILE = "X-Auth0-Token";

    // 权限认证的排序
    int OAUTH_FILTER_ORDER = -200;


    // todo 系统相关
    // 默认密码
    String INIT_USER_PASSWORD = "123456"; // "fly123456";


    // todo 接口相关
    // feign调用前缀
    public static final String FEIGN_API_PREFIX = "/feign";



    // todo 缓存相关
    // 所有菜单缓存key值
    String ALL_MENU_CACHE_KEY = "all_menu";



    // todo 时间格式相关
    // 年月日 时分秒
    String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    // todo 编码相关
    // utf-8
    String ENCODE_UTF8 = "UTF-8";


}
