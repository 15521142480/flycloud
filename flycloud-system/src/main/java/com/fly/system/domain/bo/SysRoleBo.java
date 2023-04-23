package com.fly.system.domain.bo;

import com.fly.common.database.web.domain.BaseEntity;
import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 角色信息业务对象
 *
 * @author fly
 * @date 2023-04-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleBo extends BaseEntity {

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空", groups = { EditGroup.class })
    private Long roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String roleName;

    /**
     * 角色权限字符串
     */
    @NotBlank(message = "角色权限字符串不能为空", groups = { AddGroup.class, EditGroup.class })
    private String roleKey;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer roleSort;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    @NotBlank(message = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示
     */
    @NotNull(message = "菜单树选择项是否关联显示不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示
     */
    @NotNull(message = "部门树选择项是否关联显示不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    @NotBlank(message = "角色状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
