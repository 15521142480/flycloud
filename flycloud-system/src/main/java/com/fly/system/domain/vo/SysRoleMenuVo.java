package com.fly.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 角色和菜单关联视图对象
 *
 * @author fly
 * @date 2023-04-23
 */
@Data
@ExcelIgnoreUnannotated
public class SysRoleMenuVo {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @ExcelProperty(value = "角色ID")
    private Long roleId;

    /**
     * 菜单ID
     */
    @ExcelProperty(value = "菜单ID")
    private Long menuId;


}
