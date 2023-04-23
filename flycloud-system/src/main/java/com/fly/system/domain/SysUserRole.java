package com.fly.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.database.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户和角色关联对象 sys_user_role
 *
 * @author fly
 * @date 2023-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_role")
public class SysUserRole extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id")
    private Long userId;
    /**
     * 角色ID
     */
    @TableId(value = "role_id")
    private Long roleId;

}
