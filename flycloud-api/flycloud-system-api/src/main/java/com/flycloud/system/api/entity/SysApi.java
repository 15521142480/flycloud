
package com.flycloud.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fly.common.database.entity.BaseEntity;

/**
 * 系统接口表实体类
 *
 * @author pangu
 * @since 2020-10-14
 */
@Data
@TableName("sys_api")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SysApi对象", description = "系统接口表")
public class SysApi extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	* 接口编码
	*/
	@Schema(description = "接口编码")
	private String code;
	/**
	* 接口名称
	*/
	@Schema(description = "接口名称")
	private String name;
	/**
	* 接口描述
	*/
	@Schema(description = "接口描述")
	private String notes;
	/**
	* 请求方法
	*/
	@Schema(description = "请求方法")
	private String method;
	/**
	* 类名
	*/
	@Schema(description = "类名")
	private String className;
	/**
	* 方法名
	*/
	@Schema(description = "方法名")
	private String methodName;
	/**
	* 请求路径
	*/
	@Schema(description = "请求路径")
	private String path;
	/**
	* 响应类型
	*/
	@Schema(description = "响应类型")
	private String contentType;
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
	* 是否认证:0:不认证 1:认证
	*/
	@Schema(description = "是否认证:0:不认证 1:认证")
	private String auth;
	/**
	* 删除标识
	*/
	@Schema(description = "删除标识")
	private String isDeleted;
	/**
	* 租户ID
	*/
	@Schema(description = "租户ID")
	private Integer tenantId;


}
