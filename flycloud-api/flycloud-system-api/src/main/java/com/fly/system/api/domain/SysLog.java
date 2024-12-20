
package com.fly.system.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fly.common.domain.BaseEntity;

import java.time.LocalDateTime;

/**
 * 系统日志表实体类
 *
 * @author pangu
 * @since 2020-07-15
 */
@Data
@TableName("sys_log")
@Schema(name = "SysLog对象", description = "系统日志表")
public class SysLog {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id")
	private Long id;

	/**
	* 日志类型
	*/
	@Schema(description = "日志类型")
	private String type;
	/**
	* 跟踪ID
	*/
	@Schema(description = "跟踪ID")
	private String traceId;
	/**
	* 日志标题
	*/
	@Schema(description = "日志标题")
	private String title;
	/**
	* 操作内容
	*/
	@Schema(description = "操作内容")
	private String operation;
	/**
	* 执行方法
	*/
	@Schema(description = "执行方法")
	private String method;

	/**
	 * 请求路径
	 */
	@Schema(description = "请求路径")
	private String url;
	/**
	* 参数
	*/
	@Schema(description = "参数")
	private String params;
	/**
	* ip地址
	*/
	@Schema(description = "ip地址")
	private String ip;
	/**
	* 耗时
	*/
	@Schema(description = "耗时")
	private Long executeTime;
	/**
	* 地区
	*/
	@Schema(description = "地区")
	private String location;
	/**
	* 创建人
	*/
	@Schema(description = "创建人")
	private String createBy;
	/**
	* 更新人
	*/
	@Schema(description = "更新人")
	private String updateBy;
	/**
	* 创建时间
	*/
	@Schema(description = "创建时间")
	private LocalDateTime createTime;
	/**
	* 更新时间
	*/
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;
	
	/**
	 * 异常信息
	 */
	@Schema(description = "异常信息")
	private String exception;


}
