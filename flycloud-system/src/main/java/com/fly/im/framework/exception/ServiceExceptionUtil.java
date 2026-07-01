package com.fly.im.framework.exception;

import cn.hutool.core.util.StrUtil;
import com.fly.common.exception.ServiceException;
import com.fly.system.api.im.enums.ErrorCode;

/**
 * IM 业务异常工具。
 *
 * @author lxs
 * @date 2026-07-02
 */
public class ServiceExceptionUtil {

    public static ServiceException exception(ErrorCode errorCode, Object... params) {
        String message = params == null || params.length == 0
                ? errorCode.getMsg()
                : StrUtil.format(errorCode.getMsg(), params);
        return new ServiceException(errorCode.getCode(), message);
    }

}
