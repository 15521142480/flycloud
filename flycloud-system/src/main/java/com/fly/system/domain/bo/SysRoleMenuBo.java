package com.fly.system.domain.bo;

import com.fly.common.database.web.domain.BaseEntity;
import com.fly.common.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 角色和菜单关联业务对象
 *
 * @author fly
 * @date 2023-04-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleMenuBo extends BaseEntity {

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空", groups = { EditGroup.class })
    private Long roleId;

    /**
     * 菜单ID
     */
    @NotNull(message = "菜单ID不能为空", groups = { EditGroup.class })
    private Long menuId;


}
