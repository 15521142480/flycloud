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
 * 系统黑名单表实体类
 *
 * @author pangu
 * @since 2020-08-26
 */
@Data
@TableName("mate_sys_blacklist")
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
