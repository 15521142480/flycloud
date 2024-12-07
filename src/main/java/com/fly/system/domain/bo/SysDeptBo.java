package com.fly.system.api.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 部门业务对象
 *
 * @author fly
 * @date 2024-12-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptBo extends BaseEntity {

    /**
     * 部门id
     */
    // @NotNull(message = "部门id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 部门编码
     */
    // @NotBlank(message = "部门编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 部门名称
     */
    // @NotBlank(message = "部门名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 父部门id
     */
    // @NotNull(message = "父部门id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long parentId;

    /**
     * 显示顺序
     */
    // @NotNull(message = "显示顺序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;

    /**
     * 负责人
     */
    // @NotNull(message = "负责人不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long leaderUserId;

    /**
     * 联系电话
     */
    // @NotBlank(message = "联系电话不能为空", groups = { AddGroup.class, EditGroup.class })
    private String phone;

    /**
     * 邮箱
     */
    // @NotBlank(message = "邮箱不能为空", groups = { AddGroup.class, EditGroup.class })
    private String email;

    /**
     * 部门状态（0正常 1停用）
     */
    // @NotNull(message = "部门状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;


}
