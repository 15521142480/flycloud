package com.fly.common.security.util;

import com.fly.common.entity.LoginUser;
import com.fly.common.security.user.FlyUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户工具类
 *
 */
@UtilityClass
public class UserUtils {


	/**
	 * 获取当前用户
	 */
	public FlyUser getCurUser() {

		Authentication authentication = getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof FlyUser) {
			return (FlyUser) principal;
		}

		return null;
	}

//	public LoginUser getCurUser() {
//
//		Authentication authentication = getAuthentication();
//		Object principal = authentication.getPrincipal();
//		if (principal instanceof LoginUser) {
//			return (LoginUser) principal;
//		}
//
//		return null;
//	}

	/**
	 * 获取Authentication
	 */
	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}


	/**
	 * 获取用户名称
	 *
	 * @return username
	 */
	public String getUsername() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		return authentication.getName();
	}
}
