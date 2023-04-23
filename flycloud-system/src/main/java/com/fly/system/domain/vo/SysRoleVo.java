package com.fly.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 角色信息视图对象
 *
 * @author fly
 * @date 2023-04-23
 */
@Data
@ExcelIgnoreUnannotated
public class SysRoleVo {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @ExcelProperty(value = "角色ID")
    private Long roleId;

    /**
     * 角色名称
     */
    @ExcelProperty(value = "角色名称")
    private String roleName;

    /**
     * 角色权限字符串
     */
    @ExcelProperty(value = "角色权限字符串")
    private String roleKey;

    /**
     * 显示顺序
     */
    @ExcelProperty(value = "显示顺序")
    private Integer roleSort;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    @ExcelProperty(value = "数据范围", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：全部数据权限,2=：自定数据权限,3=：本部门数据权限,4=：本部门及以下数据权限")
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示
     */
    @ExcelProperty(value = "菜单树选择项是否关联显示")
    private Integer menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示
     */
    @ExcelProperty(value = "部门树选择项是否关联显示")
    private Integer deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
