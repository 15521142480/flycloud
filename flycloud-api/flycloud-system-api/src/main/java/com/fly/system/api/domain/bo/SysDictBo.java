package com.fly.system.api.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * 字典数据业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictBo extends BaseEntity {

    /**
     * 字典编码
     */
    // @NotNull(message = "字典编码不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 字典排序
     */
    // @NotNull(message = "字典排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;

    /**
     * 字典标签
     */
    // @NotBlank(message = "字典标签不能为空", groups = { AddGroup.class, EditGroup.class })
    private String label;

    /**
     * 字典键值
     */
    // @NotBlank(message = "字典键值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String value;

    /**
     * 字典类型
     */
    // @NotBlank(message = "字典类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    // @NotNull(message = "状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 颜色类型
     */
    // @NotBlank(message = "颜色类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String colorType;

    /**
     * css 样式
     */
    // @NotBlank(message = "css 样式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cssClass;

    /**
     * 备注
     */
    // @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

}
