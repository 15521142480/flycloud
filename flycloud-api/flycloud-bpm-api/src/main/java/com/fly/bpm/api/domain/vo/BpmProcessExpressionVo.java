package com.fly.bpm.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * BPM 流程达式视图对象
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@ExcelIgnoreUnannotated
public class BpmProcessExpressionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 表达式名字
     */
    @ExcelProperty(value = "表达式名字")
    private String name;

    /**
     * 表达式状态
     */
    @ExcelProperty(value = "表达式状态")
    private Integer status;

    /**
     * 表达式
     */
    @ExcelProperty(value = "表达式")
    private String expression;

    /**
     * 是否删除
     */
    @ExcelProperty(value = "是否删除")
    private Integer isDeleted;


}
