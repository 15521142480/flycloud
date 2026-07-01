package com.fly.system.api.report.enums;

import com.fly.common.exception.ErrorCode;

/**
 * 报表模块错误码常量。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ErrorCodeConstants {

    /**
     * GoView 项目不存在。
     */
    ErrorCode GO_VIEW_PROJECT_NOT_EXISTS = new ErrorCode(1_003_000_000, "GoView 项目不存在");

}
