package com.fly.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 菜单权限视图对象
 *
 * @author fly
 * @date 2023-04-23
 */
@Data
@ExcelIgnoreUnannotated
public class SysMenuVo {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @ExcelProperty(value = "菜单ID")
    private Long menuId;

    /**
     * 菜单名称
     */
    @ExcelProperty(value = "菜单名称")
    private String menuName;

    /**
     * 父菜单ID
     */
    @ExcelProperty(value = "父菜单ID")
    private Long parentId;

    /**
     * 显示顺序
     */
    @ExcelProperty(value = "显示顺序")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @ExcelProperty(value = "路由地址")
    private String path;

    /**
     * 组件路径
     */
    @ExcelProperty(value = "组件路径")
    private String component;

    /**
     * 路由参数
     */
    @ExcelProperty(value = "路由参数")
    private String queryParam;

    /**
     * 是否为外链（0是 1否）
     */
    @ExcelProperty(value = "是否为外链", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=是,1=否")
    private Integer isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    @ExcelProperty(value = "是否缓存", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=缓存,1=不缓存")
    private Integer isCache;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @ExcelProperty(value = "菜单类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "M=目录,C=菜单,F=按钮")
    private String menuType;

    /**
     * 显示状态（0显示 1隐藏）
     */
    @ExcelProperty(value = "显示状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=显示,1=隐藏")
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    @ExcelProperty(value = "菜单状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 权限标识
     */
    @ExcelProperty(value = "权限标识")
    private String perms;

    /**
     * 菜单图标
     */
    @ExcelProperty(value = "菜单图标")
    private String icon;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
