/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fly.common.domain.model;

/**
 * <p>
 * REST API 响应码
 * </p>
 *
 * @author lxs
 */
public enum ApiCode {

    /**
     * 操作成功
     **/
    SUCCESS(200, "操作成功"),
    /**
     * 非法访问
     **/
    UNAUTHORIZED(401, "非法访问"),
    /**
     * 没有权限
     **/
    NOT_PERMISSION(403, "没有权限"),
    /**
     * 你请求的资源不存在
     **/
    NOT_FOUND(404, "你请求的资源不存在"),

    USER_ERROR(1001, "用户端错误"),
    USER_LOGIN_ERROR(1002, "用户登录异常"),

    USER_NOT_EXIST(1003, "用户不存在"),
    USER_ACCOUNT_LOCKED(1004, "用户账户被冻结"),
    USER_ACCOUNT_INVALID(1005, "用户账户已作废"),

    USERNAME_OR_PASSWORD_ERROR(1006, "用户名或密码错误"),
    PASSWORD_ENTER_EXCEED_LIMIT(1007, "用户输入密码次数超限"),
    CLIENT_AUTHENTICATION_FAILED(1008, "客户端认证失败"),
    INVALID_TOKEN(1009, "token无效或已过期"),
    TOKEN_ACCESS_FORBIDDEN(1010, "token已被禁止访问"),

    AUTHORIZED_ERROR(1011, "访问权限异常"),
    ACCESS_UNAUTHORIZED(1012, "访问未授权"),

    /**
     * 操作失败
     **/
    FAIL(500, "操作失败"),
    /**
     * 登录失败
     **/
    LOGIN_EXCEPTION(4000, "登录失败"),
    /**
     * 系统异常
     **/
    SYSTEM_EXCEPTION(5000, "系统异常"),
    /**
     * 请求参数校验异常
     **/
    PARAMETER_EXCEPTION(5001, "请求参数校验异常"),
    /**
     * 请求参数解析异常
     **/
    PARAMETER_PARSE_EXCEPTION(5002, "请求参数解析异常"),
    /**
     * HTTP内容类型异常
     **/
    HTTP_MEDIA_TYPE_EXCEPTION(5003, "HTTP内容类型异常"),
    /**
     * HTTP内容类型异常
     **/
    HTTP_CONNECT_EXCEPTION(5004, "HTTP请求连接超时"),
    /**
     * 系统处理异常
     **/
    SPRING_BOOT_PLUS_EXCEPTION(5100, "系统处理异常"),
    /**
     * 业务处理异常
     **/
    BUSINESS_EXCEPTION(5101, "业务处理异常"),
    /**
     * 数据库处理异常
     **/
    DAO_EXCEPTION(5102, "数据库处理异常"),
    /**
     * 验证码校验异常
     **/
    VERIFICATION_CODE_EXCEPTION(5103, "验证码校验异常"),
    /**
     * 登录授权异常
     **/
    AUTHENTICATION_EXCEPTION(5104, "登录授权异常"),
    /**
     * 没有访问权限
     **/
    UNAUTHENTICATED_EXCEPTION(5105, "没有访问权限"),
    /**
     * 没有访问权限
     **/
    UNAUTHORIZED_EXCEPTION(5106, "没有访问权限"),
    /**
     * JWT Token解析异常
     **/
    JWTDECODE_EXCEPTION(5107, "Token解析异常"),


    HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION(5108, "METHOD NOT SUPPORTED"),



    TIMEOUT_EXCEPTION(5110,"请求超时"),


    DEGRADATION(220, "系统功能降级"),

    ;

    private final int code;
    private final String message;

    ApiCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public static ApiCode getApiCode(int code) {
        ApiCode[] ecs = ApiCode.values();
        for (ApiCode ec : ecs) {
            if (ec.getCode() == code) {
                return ec;
            }
        }
        return SUCCESS;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
