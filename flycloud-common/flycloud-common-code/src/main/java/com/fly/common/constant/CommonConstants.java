

package com.fly.common.constant;

/**
 * @author lxs
 */
public interface CommonConstants {

    /**
     * 删除
     */
    String STATUS_DEL = "1";
    /**
     * 正常
     */
    String STATUS_NORMAL = "0";
    /**
     * 新建用户，默认密码
     */
    String USER_PASSWORD = "HZ888888";
    /**
     * 新建用户，默认密码
     */
    String USER_PASSWORD_ENCODE = "$2a$10$WyvbhINlhLQS3A0g74d7teF5iZtplYxUQXMD0rXnVejgcNQ2mct8q";
    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

    /**
     * 日志操作异常
     */
    String LOG_OPT_ERROR = "9";

    /**
     * 菜单
     */
    String MENU = "0";

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * 前端工程名
     */
    String FRONT_END_PROJECT = "micro-ui";

    /**
     * 后端工程名
     */
    String BACK_END_PROJECT = "micro";

    /**
     * 路由存放
     */
    String ROUTE_KEY = "gateway_route_key";

    /**
     * spring boot admin 事件key
     */
    String EVENT_KEY = "event_key";

    /**
     * 验证码前缀
     */
    String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY_";

    /**
     * 成功标记
     */
    Integer SUCCESS = 0;
    /**
     * 失败标记
     */
    Integer FAIL = 1;

    /**
     * 默认存储bucket
     */
    String BUCKET_NAME = "ygnet";
    /**
     * 超级管理员标识
     */
    String ADMIN_FLAG = "1";
    /**
     * 所有菜单缓存key值
     */
    String ALL_MENU_CACHE_KEY = "all_menu";

    /**
     * 菜单显示状态
     */
    String MENU_SHOW = "0";
    /**
     * 菜单隐藏状态
     */
    String MENU_HIDE = "1";
    /**
     * 菜单-系统菜单类型
     */
    String MENU_BUSS_TYPE_SYS = "0";
    /**
     * 菜单-系统模型类型
     */
    String MENU_BUSS_TYPE_MODEL = "1";
    /**
     * 模型主键ID
     */
    String SYS_MENU_MODEL_ID = "app_model_id";
    /**
     * 模型主键父ID集合
     */
    String SYS_MENU_MODEL_PARENT_IDS = "-1,app_model_id,";
    /**
     * 路由缓存开启
     */
    String KEEP_ALIVE_OPEN = "0";

    /**
     * 请求超时时间 默认为二分钟
     */
    Integer HTTP_TIMEOUT_MILLION_SECONDS = 120 * 1000;


    String HTTPS_PROTOCOL = "https";

    Integer POOL_SIZE = 20;

    /**
     * 其他异常信息
     */
    Integer DEFAULT_ERROR_CODE = 0;

    String DEFAULT_CHARSET = "UTF-8";


    String AUTHORIZATION_BEARER = "Bearer ";
    /**
     * 秘钥代码值
     */
    String SECURITY_CODE = "securityCode";
    /**
     * 操作日志类型-删除
     */
    String LOG_DELELE = "4";
    /**
     * 操作日志类型-查询
     */
    String LOG_QUERY = "1";
    /**
     * 操作日志类型-登录
     */
    String LOG_LOGIN = "0";
    /**
     * 操作日志类型-新增
     */
    String LOG_ADD = "2";
    /**
     * 操作日志类型-编辑
     */
    String LOG_EDIT = "3";
    /**
     * 匿名账号
     */
    String ANONYMOUS_USER = "anonymousUser";

    /**
     * 编辑器 请求上传图片头
     */
    String EDITOR_ACTION_IMG = "img";
    /**
     * 编辑器 请求配置信息
     */
    String EDITOR_ACTION_CONFIG = "config";
    /**
     * 编辑器 请求截图信息
     */
    String EDITOR_ACTION_SCRAWL = "scrawl";
    /**
     * 编辑器 请求抓取信息
     */
    String EDITOR_ACTION_CATHER = "cather";
    /**
     * 编辑器 请求上传音频信息
     */
    String EDITOR_ACTION_VIDEO = "video";
    /**
     * 编辑器 请求上传图片头
     */
    String EDITOR_ACTION_FILE = "file";
    /**
     * 编辑器 请求图片列表信息
     */
    String EDITOR_ACTION_LIST_IMG = "listImg";
    /**
     * 编辑器 请求文件列表信息
     */
    String EDITOR_ACTION_LIST_FILE = "listFile";
    /**
     * 编辑器处理失败
     */
    String EDITOR_SUCCESS = "SUCCESS";

    String EDITOR_BUCKET_NAME = "u-editor";
    /**
     * 门户系统菜单
     */
    String MENU_TYPE_SYSTEM = "0";
    /**
     * 缉私系统菜单
     */
    String MENU_TYPE_SASS = "2";

    /**
     * 移动端接口请求头
     */
    String AUTHORIZATION_MOBILE = "X-Auth0-Token";

    String SYS_USER_TB = "sys_user";
    String SYS_DEPT_TB = "sys_dept";

    /**
     * 烽火接口请求头
     */
    String ACCESS_TOKEN = "access_token";
    /**
     * 应用工具角色权限
     */
    String APP_TOOL_ROLE="app_tool-";

    /**
     * 应用工具缓存信息
     */
    String APP_TOOL_CACHE_NAME = "app_tool_cache";
}
