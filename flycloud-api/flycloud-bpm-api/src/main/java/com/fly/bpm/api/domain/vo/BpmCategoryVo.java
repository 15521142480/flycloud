package com.fly.bpm.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * BPM 流程分类视图对象
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@ExcelIgnoreUnannotated
public class BpmCategoryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类编号
     */
    @ExcelProperty(value = "分类编号")
    private Long id;

    /**
     * 分类名
     */
    @ExcelProperty(value = "分类名")
    private String name;

    /**
     * 分类标志
     */
    @ExcelProperty(value = "分类标志")
    private String code;

    /**
     * 分类描述
     */
    @ExcelProperty(value = "分类描述")
    private String description;

    /**
     * 分类状态
     */
    @ExcelProperty(value = "分类状态")
    private Integer status;

    /**
     * 分类排序
     */
    @ExcelProperty(value = "分类排序")
    private Long sort;

    /**
     * 是否删除
     */
    @ExcelProperty(value = "是否删除")
    private Integer isDeleted;


}
