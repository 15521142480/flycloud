
package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fly.common.database.entity.BaseEntity;

/**
 * 系统黑名单表实体类
 *
 * @author pangu
 * @since 2020-08-26
 */
@Data
@TableName("sys_blacklist")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SysBlacklist对象", description = "系统黑名单表")
public class SysBlacklist extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	* IP地址
	*/
	@Schema(description = "IP地址")
	private String ip;
	/**
	* 请求地址
	*/
	@Schema(description = "请求地址")
	private String requestUri;
	/**
	* 请求方法
	*/
	@Schema(description = "请求方法")
	private String requestMethod;
	/**
	* 开始时间
	*/
	@Schema(description = "开始时间")
	private String startTime;
	/**
	* 结束时间
	*/
	@Schema(description = "结束时间")
	private String endTime;
	/**
	* 状态：0:关闭 1:开启
	*/
	@Schema(description = "状态：0:关闭 1:开启")
	private String status;

}
