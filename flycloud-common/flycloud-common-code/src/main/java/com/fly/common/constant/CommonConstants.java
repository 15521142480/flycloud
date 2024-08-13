

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


    // todo 授权相关
    // 请求头 header
    String HEADER = "header";

    // 请求头-授权key
    String AUTHORIZATION_KEY = "Authorization";

    // 请求头-授权-前缀 Bearer
    String AUTHORIZATION_BEARER = "Bearer ";

    // 移动端接口请求头
    String AUTHORIZATION_MOBILE = "X-Auth0-Token";


    // todo 系统相关
    // 超级管理员标识
    String ADMIN_FLAG = "1";

    // 请求超时时间 默认为二分钟
    Integer HTTP_TIMEOUT_MILLION_SECONDS = 120 * 1000;

    // 新建用户，默认密码
    String USER_PASSWORD = "HZ888888";

    // 新建用户，默认密码
    String USER_PASSWORD_ENCODE = "$2a$10$WyvbhINlhLQS3A0g74d7teF5iZtplYxUQXMD0rXnVejgcNQ2mct8q";



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
