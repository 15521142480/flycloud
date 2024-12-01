package com.fly.bpm.api.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * BPM 流程分类业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BpmCategoryBo extends BaseEntity {

    /**
     * 分类编号
     */
    // @NotNull(message = "分类编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 分类名
     */
    // @NotBlank(message = "分类名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 分类标志
     */
    // @NotBlank(message = "分类标志不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 分类描述
     */
    // @NotBlank(message = "分类描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;

    /**
     * 分类状态
     */
    // @NotNull(message = "分类状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 分类排序
     */
    // @NotNull(message = "分类排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;


}
