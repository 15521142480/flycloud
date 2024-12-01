package com.fly.system.api.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * 角色业务对象
 *
 * @author fly
 * @date 2024-08-31
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleBo extends BaseEntity {

    /**
     * 主键id
     */
    // @NotNull(message = "主键id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 角色类型（sys_type）
     */
    // @NotNull(message = "角色类型（sys_type）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long type;

    /**
     * 角色名称
     */
    // @NotBlank(message = "角色名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 角色编码
     */
    // @NotBlank(message = "角色编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 描述
     */
    // @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 排序
     */
    // @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;

    /**
     * 状态
     */
    // @NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;


    /**
     * 角色菜单权限信息；permisson 与 menuId 拼接的json字符串数据
     */
//     @NotNull(message = "角色菜单权限信息不能为空", groups = { AddGroup.class, EditGroup.class })
    private String roleMenuPermissionJson;

}
