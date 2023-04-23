package com.fly.system.domain.bo;

import com.fly.common.database.web.domain.BaseEntity;
import com.fly.common.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 用户和角色关联业务对象
 *
 * @author fly
 * @date 2023-04-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserRoleBo extends BaseEntity {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空", groups = { EditGroup.class })
    private Long userId;

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空", groups = { EditGroup.class })
    private Long roleId;


}
