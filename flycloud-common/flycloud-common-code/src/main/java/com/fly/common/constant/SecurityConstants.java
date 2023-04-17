

package com.fly.common.constant;

/**
 * @author ygnet
 * @date 2017-12-18
 */
public interface SecurityConstants {
    /**
     * 刷新
     */
    String REFRESH_TOKEN = "refresh_token";
    /**
     * 验证码有效期
     */
    int CODE_TIME = 180;
    /**
     * 验证码长度
     */
    String CODE_SIZE = "4";
    /**
     * 角色前缀
     */
    String ROLE = "ROLE_";
    /**
     * 前缀
     */
    String MICRO_PREFIX = "micro_";

    /**
     * oauth 相关前缀
     */
    String OAUTH_PREFIX = "oauth:";
    /**
     * 项目的license
     */
    String MICRO_LICENSE = "made by sunshine boy";

    /**
     * 内部
     */
    String FROM_IN = "Y";

    /**
     * 标志(内部调用、泄露掉这个值相当于所有内部调用值均泄密)
     */
    String FROM = "micro_inner_security";
    /**
     * 统单点登录URL地址转接
     */
    String CAS_URL="/cas/sso";

    /**
     * OAUTH URL
     */
    String OAUTH_TOKEN_URL = "/oauth";

    /**
     * 手机号登录URL
     */
    String SMS_TOKEN_URL = "/mobile/token/sms";

    /**
     * 社交登录URL
     */
    String SOCIAL_TOKEN_URL = "/mobile/token/social";
    /**
     * 自定义登录URL
     */
    String MOBILE_TOKEN_URL = "/mobile/token/*";

    /**
     * PKI 证书登录
     */
    String PKI_TOKEN_URL = "/pki/token/certificate";
    /**
     * oauth 客户端信息
     */
    String CLIENT_DETAILS_KEY = "micro_oauth:client:details";

    /**
     * 微信获取OPENID
     */
    String WX_AUTHORIZATION_CODE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token" +
            "?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * {bcrypt} 加密的特征码
     */
    String BCRYPT = "{bcrypt}";
    /**
     * sys_oauth_client_details 表的字段，不包括client_id、client_secret
     */
    String CLIENT_FIELDS = "client_id, CONCAT('{noop}',client_secret) as client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    /**
     * JdbcClientDetailsService 查询语句
     */
    String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS
            + " from sys_oauth_client_details";

    /**
     * 默认的查询语句
     */
    String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

    /**
     * 按条件client_id 查询
     */
    String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

    /**
     * 资源服务器默认bean名称
     */
    String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";

    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";
    /**
     * 用户ID
     */
    String TOKEN_USER_ID="user_id";
    /**
     * 用户名称
     */
    String TOKEN_USER_NAME="username";
    /**
     * 用户部门ID
     */
    String TOKEN_DEPT_ID="dept_id";

}
