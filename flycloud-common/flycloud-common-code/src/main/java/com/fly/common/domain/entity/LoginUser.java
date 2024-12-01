package com.fly.common.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录用户对象
 *
 */
@Data
public class LoginUser implements Serializable {

	private static final long serialVersionUID = 1599282604110237521L;

	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 部门id
	 */
	private String deptId;
	/**
	 * 岗位id
	 */
	private String postId;
	/**
	 * 角色id
	 */
	private String roleId;
	/**
	 * 角色名
	 */
	private String roleName;

	/**
	 * 用户类型
	 */
	private int userType;

	/**
	 * 登录类型　1：用户名密码登录　2：手机号登录　3：社交登录 ...
	 */
	private int loginType;

}
