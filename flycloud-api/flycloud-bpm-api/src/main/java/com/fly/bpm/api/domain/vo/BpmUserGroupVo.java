package com.fly.bpm.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * BPM 用户组视图对象
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@ExcelIgnoreUnannotated
public class BpmUserGroupVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 组名
     */
    @ExcelProperty(value = "组名")
    private String name;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;

    /**
     * 成员编号数组
     */
    @ExcelProperty(value = "成员编号数组")
    private String userIds;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private Integer status;

    /**
     * 是否删除
     */
    @ExcelProperty(value = "是否删除")
    private Integer isDeleted;


}
