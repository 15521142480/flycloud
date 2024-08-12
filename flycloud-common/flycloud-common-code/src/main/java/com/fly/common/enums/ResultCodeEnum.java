package com.fly.common.enums;

/**
 * 结果返回状态 - 枚举
 *
 * @author: lxs
 * @date: 2024/8/8
 */
public enum ResultCodeEnum {


    SUCCESS(200, "操作成功"),
    UNAUTHORIZED(401, "登录超时"),
    PASSWORD_TRY_MAX_ERROR(402, "密码尝试次数过多，账号已被锁定"),
    NO_PERMISSION(403, "当前用户无该接口权限"),
    FAILED(410, "操作失败"),
    PARAM_ERROR(411, "参数错误"),
    INVALID_PARAM_EXIST(412, "请求参数已存在"),
    INVALID_PARAM_EMPTY(413, "请求参数为空"),
    PARAM_TYPE_MISMATCH(414, "参数类型不匹配"),
    PARAM_VALID_ERROR(415, "参数校验失败"),
    ILLEGAL_REQUEST(416, "非法请求"),
    INVALID_CAPTCHA_TYPE(417, "验证码类型不匹配"),
    INVALID_CAPTCHA(418, "验证码错误"),
    INVALID_USERNAME_PASSWORD(419, "账号或密码错误"),
    INVALID_RE_PASSWORD(420, "两次输入密码不一致"),
    INVALID_OLD_PASSWORD(421, "旧密码错误"),
    USERNAME_ALREADY_IN(422, "用户名已存在"),
    INVALID_USERNAME(423, "用户名不存在"),
    INVALID_ROLE(424, "角色不存在"),
    ROLE_USED(425, "角色使用中，不可删除"),
    TENANT_NOT_FOUND(426, "已开启租户模式，未在请求中查询到租户信息"),
    INVALID_PASSWORD_CAPTCHA(427, "密码错误次数超过最大限值，请进行安全认证"),
    DISABLED_ACCOUNT(428, "账号已被禁用"),
    SYSTEM_BUSY(429, "系统繁忙，请稍后重试"),
    RESUBMIT_LOCK(430, "您的操作请求已提交，请勿重复提交"),
    ERROR(500, "系统错误"),
    BIND_NOT_FOUND(601, "未找到绑定关系"),
    BIND_MULTIPLE(602, "当前有第三方用户已绑定多个用户"),
    BIND_ALREADY(603, "当前用户已绑定，请先到个人中心解绑"),
    SMS_LIMIT(701, "当前手机号超出最大发送短信条数"),
    SMS_SEND_FAILED(702, "短信发送失败"),
    SMS_SEND_ERROR(703, "短信发送发生错误"),
    SMS_TEMPLATE_NOT_FOUND(704, "未找到符合条件的短信模板"),
    SMS_CODE_EXPIRE(705, "验证码已过期"),
    SMS_CODE_ERROR(706, "验证码错误"),
    SMS_CODE_NOT_EMPTY(707, "验证码不能为空"),
    SMS_CODE_EXPIRE_OR_EXPIRE(708, "验证码错误或者已过期，请重新输入或获取"),

    USER_ACCOUNT_EXPIRED(11008, "用户账号过期"),
    USER_PASSWORD_EXPIRED(11007, "用户密码过期"),
    USER_LOGIN_FAIL(11010, "用户登录失败"),
    ;

    public int code;
    public String msg;

    private ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
