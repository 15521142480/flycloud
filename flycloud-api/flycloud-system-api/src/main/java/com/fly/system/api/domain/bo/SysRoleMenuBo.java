package com.fly.system.api.domain.bo;

import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.fly.common.database.web.domain.BaseEntity;
import lombok.experimental.Accessors;

/**
 * 角色权限业务对象
 *
 * @author fly
 * @date 2024-08-31
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysRoleMenuBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 角色id
     */
    // @NotNull(message = "角色id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long roleId;

    /**
     * 菜单id
     */
    // @NotNull(message = "菜单id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long menuId;

    /**
     * 权限
     */
    // @NotBlank(message = "权限不能为空", groups = { AddGroup.class, EditGroup.class })
    private String permission;


}
