package com.fly.im.framework.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * IM 错误码。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@AllArgsConstructor
public class ErrorCode {

    private Integer code;

    private String msg;

}
