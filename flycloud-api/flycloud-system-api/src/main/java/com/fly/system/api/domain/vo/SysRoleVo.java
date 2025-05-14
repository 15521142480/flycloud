package com.fly.system.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 角色视图对象
 *
 * @author fly
 * @date 2024-08-31
 */
@Data
@ExcelIgnoreUnannotated
public class SysRoleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ExcelProperty(value = "主键id")
    private Long id;

    /**
     * 角色类型（sys_type）
     */
    @ExcelProperty(value = "角色类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ys_type")
    private Long type;

    /**
     * 角色名称
     */
    @ExcelProperty(value = "角色名称")
    private String name;

    /**
     * 角色编码
     */
    @ExcelProperty(value = "角色编码")
    private String code;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String remark;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sort;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Integer status;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
