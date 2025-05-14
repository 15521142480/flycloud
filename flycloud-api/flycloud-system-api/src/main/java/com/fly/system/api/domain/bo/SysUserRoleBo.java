package com.fly.system.api.domain.bo;

import com.fly.common.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import com.fly.common.domain.BaseEntity;

import java.util.List;

/**
 * 用户角色业务对象
 *
 * @author fly
 * @date 2024-08-31
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserRoleBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户id
     */
    // @NotNull(message = "用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 角色id
     */
    // @NotNull(message = "角色id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long roleId;

    /**
     * 角色ids
     */
    private List<Long> roleIds;


}
