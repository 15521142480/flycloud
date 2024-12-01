package com.fly.system.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 角色权限视图对象
 *
 * @author fly
 * @date 2024-08-31
 */
@Data
@ExcelIgnoreUnannotated
public class SysRoleMenuVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 角色id
     */
    @ExcelProperty(value = "角色id")
    private Long roleId;

    /**
     * 菜单id
     */
    @ExcelProperty(value = "菜单id")
    private Long menuId;

    /**
     * 权限
     */
    @ExcelProperty(value = "权限")
    private String permission;


}
