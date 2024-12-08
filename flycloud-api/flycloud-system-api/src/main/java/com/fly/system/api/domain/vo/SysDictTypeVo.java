package com.fly.system.api.domain.vo;

import java.time.LocalDateTime;
import java.io.Serializable;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 字典类型视图对象
 *
 * @author fly
 * @date 2024-12-08
 */
@Data
@ExcelIgnoreUnannotated
public class SysDictTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    @ExcelProperty(value = "字典主键")
    private Long id;

    /**
     * 字典名称
     */
    @ExcelProperty(value = "字典名称")
    private String name;

    /**
     * 字典类型
     */
    @ExcelProperty(value = "字典类型")
    private String type;

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
     * 是否删除
     */
    @ExcelProperty(value = "是否删除")
    private Integer isDeleted;

    /**
     * 删除时间
     */
    @ExcelProperty(value = "删除时间")
    private LocalDateTime deletedTime;


}
