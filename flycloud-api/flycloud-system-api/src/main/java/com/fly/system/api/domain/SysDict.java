package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * 字典数据对象 sys_dict
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict")
public class SysDict extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 字典编码
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 字典排序
     */
    private Long sort;
    /**
     * 字典标签
     */
    private String label;
    /**
     * 字典键值
     */
    private String value;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
    /**
     * 颜色类型
     */
    private String colorType;
    /**
     * css 样式
     */
    private String cssClass;
    /**
     * 备注
     */
    private String remark;
}
