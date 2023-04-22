package com.fly.common.excel;

import java.util.List;

/**
 * excel返回对象
 *
 * @author lxs
 */
public interface ExcelResult<T> {

    /**
     * 标题
     */
    void setTitle(String title);

    /**
     * 标题
     */
    String getTitle();

    /**
     * 对象列表
     */
    List<T> getList();

    /**
     * 错误列表
     */
    List<String> getErrorList();

    /**
     * 导入回执
     */
    String getAnalysis();
}
