

package com.fly.common.model;

import com.fly.common.constant.CommonConstants;
import com.fly.common.utils.StringUtils;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author lxs
 */
@ToString
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class R<T> implements Serializable {


    private static final long serialVersionUID = 1L;

    private static final String SUCCESS_STRING_STR = "成功";

    /**
     * 响应code
     */
    @Getter
    @Setter
    private int code;

    /**
     * 响应消息
     */
    @Getter
    @Setter
    private String msg;

    /**
     * 响应数据
     */
    @Getter
    @Setter
    private T data;


    public static <T> R<T> ok() {
        return restResult(null, CommonConstants.SUCCESS, SUCCESS_STRING_STR);
    }

    /**
     * 响应返回结果 (适用于增删改)
     */
    public static <T> R<T> ok(Boolean result) {
        return result ? R.ok() : R.failed();
    }

    /**
     * 响应返回结果 (适用于增删改)
     */
    public static <T> R<T> ok(int rows) {
        return rows > 0 ? R.ok() : R.failed();
    }
    public static <T> R<T> ok(Integer rows) {
        return rows > 0 ? R.ok() : R.failed();
    }


    public static <T> R<T> ok(T data) {
        return restResult(data, CommonConstants.SUCCESS, SUCCESS_STRING_STR);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, CommonConstants.SUCCESS, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, CommonConstants.FAIL, "操作失败");
    }

    public static <T> R<T> failed(Integer code, String message) {
        return restResult(null, CommonConstants.FAIL, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, CommonConstants.FAIL, msg);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, CommonConstants.FAIL, null);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, CommonConstants.FAIL, msg);
    }


    /**
     * 结果响应组装
     */
    private static <T> R<T> restResult(T data, int code, String msg) {

        if (StringUtils.isBlank(msg)) {
            msg = SUCCESS_STRING_STR;
        }
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }


    public R() {
        super();
    }

    public R(T data) {
        super();
        this.data = data;
    }

    public R(T data, String msg) {
        super();
        this.data = data;
        this.msg = msg;
    }
}
