package com.fly.common.context;


import com.fly.common.entity.LoginUser;

/**
 * 用户上下文
 *
 * @author pangu
 */
public class UserContext {


	private static ThreadLocal<LoginUser> userHolder = new ThreadLocal<LoginUser>();


	public static void setUser(LoginUser loginUser) {
		userHolder.set(loginUser);
	}


	public static LoginUser getUser() {
		return userHolder.get();
	}

}
