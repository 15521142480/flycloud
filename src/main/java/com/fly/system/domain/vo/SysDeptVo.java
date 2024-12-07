package com.fly.system.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 部门视图对象
 *
 * @author fly
 * @date 2024-12-01
 */
@Data
@ExcelIgnoreUnannotated
public class SysDeptVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    @ExcelProperty(value = "部门id")
    private Long id;

    /**
     * 部门编码
     */
    @ExcelProperty(value = "部门编码")
    private String code;

    /**
     * 部门名称
     */
    @ExcelProperty(value = "部门名称")
    private String name;

    /**
     * 父部门id
     */
    @ExcelProperty(value = "父部门id")
    private Long parentId;

    /**
     * 显示顺序
     */
    @ExcelProperty(value = "显示顺序")
    private Long sort;

    /**
     * 负责人
     */
    @ExcelProperty(value = "负责人")
    private Long leaderUserId;

    /**
     * 联系电话
     */
    @ExcelProperty(value = "联系电话")
    private String phone;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱")
    private String email;

    /**
     * 部门状态（0正常 1停用）
     */
    @ExcelProperty(value = "部门状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private Integer status;

    /**
     * 是否删除
     */
    @ExcelProperty(value = "是否删除")
    private Integer isDeleted;


}
