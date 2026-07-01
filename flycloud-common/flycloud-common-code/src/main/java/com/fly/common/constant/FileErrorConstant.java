package com.fly.common.constant;

import com.fly.common.exception.ErrorCode;

/**
 * 文件错误常量
 *
 * @author: lxs
 * @date: 2026/7/1
 */
public interface FileErrorConstant {

    ErrorCode FILE_PATH_EXISTS = new ErrorCode(1_001_003_000, "文件路径已存在");
    ErrorCode FILE_NOT_EXISTS = new ErrorCode(1_001_003_001, "文件不存在");
    ErrorCode FILE_IS_EMPTY = new ErrorCode(1_001_003_002, "文件为空");
    ErrorCode FILE_PATH_INVALID = new ErrorCode(1_001_003_003, "文件路径不正确");
}
