package com.fly.bpm.api.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * BPM 用户组业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BpmUserGroupBo extends BaseEntity {

    /**
     * 编号
     */
    // @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 组名
     */
    // @NotBlank(message = "组名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 描述
     */
    // @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;

    /**
     * 成员编号数组
     */
    // @NotBlank(message = "成员编号数组不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userIds;

    /**
     * 状态（0正常 1停用）
     */
    // @NotNull(message = "状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;


}
