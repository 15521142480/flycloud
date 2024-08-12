/*
 * Copyright 2020-2030, MateCloud, DAOTIANDI Technology Inc All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Author: pangu(7333791@qq.com)
 */
package com.flycloud.system.api.entity;

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
@TableName("mate_sys_route")
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
	/**
	* 租户ID
	*/
	@Schema(description = "租户ID")
	private Integer tenantId;


}
