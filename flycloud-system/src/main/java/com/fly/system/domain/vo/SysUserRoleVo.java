package com.fly.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 用户和角色关联视图对象
 *
 * @author fly
 * @date 2023-04-23
 */
@Data
@ExcelIgnoreUnannotated
public class SysUserRoleVo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 角色ID
     */
    @ExcelProperty(value = "角色ID")
    private Long roleId;


}
