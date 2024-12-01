package com.fly.system.api.domain.common;

import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.vo.SysMenuTreeVo;
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

	// ========================================

	/**
	 * 用户名
	 */
	@Schema(description = "用户名")
	private String userName;

	/**
	 * 用户昵称
	 */
	@Schema(description = "用户昵称")
	private String nickName;

	/**
	 * 登录类型　1：用户名密码登录　2：手机号登录　3：社交登录 ...
	 */
	private int loginType;


	// ======================================== 菜单与权限
	/**
	 * 系统权限标识组id
	 */
	@Schema(description = "系统权限标识组id")
	private String roleIds;

	/**
	 * 系统权限标识组
	 */
	@Schema(description = "系统权限标识组")
	private List<String> permissionList;


}
