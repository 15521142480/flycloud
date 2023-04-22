package com.fly.common.excel;

import com.alibaba.excel.read.listener.ReadListener;

/**
 * Excel 导入监听
 *
 * @author lxs
 */
public interface ExcelListener<T> extends ReadListener<T> {

    /**
     * 获取导入结果
     * @return ExcelResult
     */
    ExcelResult<T> getExcelResult();

}
