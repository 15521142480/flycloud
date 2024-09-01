package com.fly.system.api.domain.common;

import com.fly.system.api.domain.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 *
 */
@Data
@Schema(name = "用户信息封装")
public class UserInfo implements Serializable {

	private static final long serialVersionUID = -7657663783681647907L;

	/**
	 * 系统用户信息
	 */
	@Schema(description = "系统用户信息")
	private SysUser sysUser;

	/**
	 * 系统权限标识组
	 */
	@Schema(description = "系统权限标识组")
	private List<String> permissions;

	/**
	 * 系统角色标识组
	 */
	@Schema(description = "系统角色标识组")
	private List<String> roleIds;

	/**
	 * 用户名
	 */
	@Schema(description = "用户名")
	private String userName;

	/**
	 * 用户列类型
	 */
	private int userType;

	/**
	 * 登录类型　1：用户名密码登录　2：手机号登录　3：社交登录 ...
	 */
	private int loginType;



}
