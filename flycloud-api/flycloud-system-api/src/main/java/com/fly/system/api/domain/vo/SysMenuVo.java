package com.fly.system.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 菜单视图对象
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@ExcelIgnoreUnannotated
public class SysMenuVo implements Serializable {

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
     * 菜单名称
     */
    @ExcelProperty(value = "菜单名称")
    private String name;

    /**
     * 父菜单ID
     */
    @ExcelProperty(value = "父菜单ID")
    private Long parentId;

    /**
     * 菜单权限
     */
    @ExcelProperty(value = "菜单权限")
    private String permission;

    /**
     * 按钮权限
     */
    @ExcelProperty(value = "按钮权限")
    private String buttonPermission;

    /**
     * 排序值
     */
    @ExcelProperty(value = "排序值")
    private Long sort;

    /**
     * 菜单等级
     */
    @ExcelProperty(value = "菜单等级")
    private Long level;

    /**
     * 路由路径
     */
    @ExcelProperty(value = "路由路径")
    private String path;

    /**
     * 菜单图标
     */
    @ExcelProperty(value = "菜单图标")
    private String icon;

    /**
     * 组件路径
     */
    @ExcelProperty(value = "组件路径")
    private String component;

    /**
     * 组件名
     */
    @ExcelProperty(value = "组件名")
    private String componentName;

    /**
     * 状态（0:启用，1:禁用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=:启用，1:禁用")
    private Integer status;

    /**
     * 是否可见
     */
    @ExcelProperty(value = "是否可见")
    private Integer visible;

    /**
     * 是否缓存该页面: 1:是  0:不是
     */
    @ExcelProperty(value = "是否缓存该页面: 1:是  0:不是")
    private Integer keepAlive;

    /**
     * 是否总是显示
     */
    @ExcelProperty(value = "是否总是显示")
    private Integer alwaysShow;

    /**
     * 是否外链
     */
    @ExcelProperty(value = "是否外链")
    private Integer target;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}
