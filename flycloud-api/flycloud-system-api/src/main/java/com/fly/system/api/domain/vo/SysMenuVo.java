package com.fly.system.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 菜单视图对象
 *
 * @author fly
 * @date 2024-08-31
 */
@Data
@ExcelIgnoreUnannotated
public class SysMenuVo {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @ExcelProperty(value = "菜单ID")
    private Long id;

    /**
     * 菜单类型（sys_type）
     */
    @ExcelProperty(value = "菜单类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ys_type")
    private Long type;

    /**
     * 菜单标题
     */
    @ExcelProperty(value = "菜单标题")
    private String name;

    /**
     * 父菜单ID
     */
    @ExcelProperty(value = "父菜单ID")
    private Long parentId;

    /**
     * 菜单
     */
    @ExcelProperty(value = "菜单")
    private String permission;

    /**
     * 按钮权限
     */
    @ExcelProperty(value = "按钮权限")
    private String buttonPermission;

    /**
     * 菜单等级
     */
    private Long level;

    /**
     * 路由路径
     */
    @ExcelProperty(value = "路由路径")
    private String path;

    /**
     * 组件路径
     */
    @ExcelProperty(value = "组件路径")
    private String component;

    /**
     * 菜单图标
     */
    @ExcelProperty(value = "菜单图标")
    private String icon;

    /**
     * 状态（0:启用，1:禁用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=:启用，1:禁用")
    private String status;

    /**
     * 排序值
     */
    @ExcelProperty(value = "排序值")
    private Long sort;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 是否缓存该页面: 1:是  0:不是
     */
    @ExcelProperty(value = "是否缓存该页面: 1:是  0:不是")
    private Integer keepAlive;

    /**
     * 是否隐藏
     */
    @ExcelProperty(value = "是否隐藏")
    private Integer hidden;

    /**
     * 是否外链
     */
    @ExcelProperty(value = "是否外链")
    private Integer target;

    /**
     * 删除标识
     */
    @ExcelProperty(value = "删除标识")
    private Integer isDeleted;




}
