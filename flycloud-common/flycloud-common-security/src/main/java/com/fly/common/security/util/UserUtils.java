package com.fly.common.security.util;

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
	public static FlyUser getCurUser() {

		Authentication authentication = getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof FlyUser) {
			return (FlyUser) principal;
		}

		return null;
	}

	/**
	 * 获取当前用户id
	 */
	public static Long getCurUserId() {

		FlyUser flyUser = getCurUser();
		return flyUser != null ? flyUser.getId() : null;
	}

	/**
	 * 获取用户名称
	 *
	 * @return username
	 */
	public static String getUsername() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		return authentication.getName();
	}


	/**
	 * 获取Authentication
	 */
	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

}
