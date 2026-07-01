package com.fly.system.api.im.enums;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * IM 错误码。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@AllArgsConstructor
public class ErrorCode {

    private Integer code;

    private String msg;

}
