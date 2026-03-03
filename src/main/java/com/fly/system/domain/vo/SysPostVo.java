package com.fly.system.api.domain.vo;

import java.io.Serializable;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;



/**
 * 岗位视图对象
 *
 * @author fly
 * @date 2026-03-02
 */
@Data
@ExcelIgnoreUnannotated
public class SysPostVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID
     */
    @ExcelProperty(value = "岗位ID")
    private Long id;

    /**
     * 岗位编码
     */
    @ExcelProperty(value = "岗位编码")
    private String code;

    /**
     * 岗位名称
     */
    @ExcelProperty(value = "岗位名称")
    private String name;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sort;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private Integer status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 删除标识
     */
    @ExcelProperty(value = "删除标识")
    private Integer isDeleted;


}
