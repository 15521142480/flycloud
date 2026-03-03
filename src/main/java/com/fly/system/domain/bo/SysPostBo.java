package com.fly.system.api.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * 岗位业务对象
 *
 * @author fly
 * @date 2026-03-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysPostBo extends BaseEntity {

    /**
     * 岗位ID
     */
    // @NotNull(message = "岗位ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 岗位编码
     */
    // @NotBlank(message = "岗位编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 岗位名称
     */
    // @NotBlank(message = "岗位名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 排序
     */
    // @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;

    /**
     * 状态（0正常 1停用）
     */
    // @NotNull(message = "状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 备注
     */
    // @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
