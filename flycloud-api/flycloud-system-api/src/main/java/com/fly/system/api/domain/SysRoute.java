
package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fly.common.database.entity.BaseEntity;

/**
 * 系统路由表实体类
 *
 * @author pangu
 * @since 2020-10-17
 */
@Data
@TableName("sys_route")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SysRoute对象", description = "系统路由表")
public class SysRoute extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	* 接口名称
	*/
	@Schema(description = "接口名称")
	private String name;
	/**
	* 路径前缀
	*/
	@Schema(description = "路径前缀")
	private String path;
	/**
	* 地址
	*/
	@Schema(description = "地址")
	private String url;
	/**
	* 服务ID
	*/
	@Schema(description = "服务ID")
	private String serviceId;
	/**
	* API状态:0:禁用 1:启用
	*/
	@Schema(description = "API状态:0:禁用 1:启用")
	private String status;
	/**
	* 删除标识
	*/
	@Schema(description = "删除标识")
	private String isDeleted;

}
