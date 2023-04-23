package com.fly.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.database.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色信息对象 sys_role
 *
 * @author fly
 * @date 2023-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 角色ID
     */
    @TableId(value = "role_id")
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色权限字符串
     */
    private String roleKey;
    /**
     * 显示顺序
     */
    private Integer roleSort;
    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    private String dataScope;
    /**
     * 菜单树选择项是否关联显示
     */
    private Integer menuCheckStrictly;
    /**
     * 部门树选择项是否关联显示
     */
    private Integer deptCheckStrictly;
    /**
     * 角色状态（0正常 1停用）
     */
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;
    /**
     * 备注
     */
    private String remark;

}
