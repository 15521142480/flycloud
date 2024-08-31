
package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fly.common.database.entity.BaseEntity;

/**
 * 角色和部门关联表实体类
 *
 * @author pangu
 * @since 2021-04-05
 */
@Data
@TableName("role_depart")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SysRoleDepart对象", description = "角色和部门关联表")
public class SysRoleDepart extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	* 角色ID
	*/
	@Schema(description = "角色ID")
	private Long roleId;
	/**
	* 部门ID
	*/
	@Schema(description = "部门ID")
	private Long departId;


}
