package com.fly.system.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;

import java.io.Serializable;


/**
 * 字典数据视图对象
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@ExcelIgnoreUnannotated
public class SysDictDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    @ExcelProperty(value = "字典编码")
    private Long id;

    /**
     * 字典排序
     */
    @ExcelProperty(value = "字典排序")
    private Long sort;

    /**
     * 字典标签
     */
    @ExcelProperty(value = "字典标签")
    private String label;

    /**
     * 字典键值
     */
    @ExcelProperty(value = "字典键值")
    private String value;

    /**
     * 字典类型
     */
    @ExcelProperty(value = "字典类型")
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private Integer status;

    /**
     * 颜色类型
     */
    @ExcelProperty(value = "颜色类型")
    private String colorType;

    /**
     * css 样式
     */
    @ExcelProperty(value = "css 样式")
    private String cssClass;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
