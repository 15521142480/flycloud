package com.fly.common.utils.auth;

import com.fly.common.constant.Oauth2Constants;
import com.fly.common.domain.entity.LoginUser;
import com.fly.common.exception.TokenException;
import com.fly.common.utils.NumberUtil;
import com.fly.common.utils.StringUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * token安全检测工具类
 *
 */
@Slf4j
public class SecurityUtils {


	/**
	 * 从request里获取token 
	 * 
	 * <p> 
	 * 有错抛有异常
	 *
	 * @param request HttpServletRequest
	 */
	public static String getToken(HttpServletRequest request) {

		String headerToken = getHeaderToken(request);
		if (StringUtils.isBlank(headerToken)) {
			throw new TokenException("没有携带Token信息！");
		}
		return StringUtils.isNotBlank(headerToken) ? TokenUtils.getToken(headerToken) : "";
	}
	
	/**
	 * 从request里获取token
	 *
	 * @param request HttpServletRequest
	 */
	public static String getHeaderToken(HttpServletRequest request) {
		
		return request.getHeader(Oauth2Constants.HEADER_TOKEN_KEY);
	}


	/**
	 * 从request获取当前LoginUser信息
	 *
	 * @param request HttpServletRequest
	 */
	public static LoginUser getCurUser(HttpServletRequest request) {

		String token = getToken(request);
		Claims claims = getClaims(token);

		// 根据token获取用户登录信息，这里省略获取用户信息的过程
		LoginUser loginUser = new LoginUser();
		loginUser.setUserId(Long.valueOf(String.valueOf(claims.get(Oauth2Constants.USER_ID))));
		loginUser.setAccount((String) claims.get(Oauth2Constants.USER_NAME));
		loginUser.setUserType(NumberUtil.stoi(String.valueOf(claims.get(Oauth2Constants.USER_TYPE))));
		loginUser.setLoginType(NumberUtil.stoi(String.valueOf(claims.get(Oauth2Constants.LOGIN_TYPE))));
		loginUser.setRoleId(String.valueOf(claims.get(Oauth2Constants.ROLE_IDS)));

		return loginUser;
	}

	/**
	 * 获取当前用户id
	 *
	 * @param request HttpServletRequest
	 */
	public static Long getCurUserId(HttpServletRequest request) {

		LoginUser loginUser = getCurUser(request);
		return loginUser.getUserId();
	}

	/**
	 * 从Token解析获取Claims对象
	 *
	 * @param token Authorization获取的token
	 */
	public static Claims getClaims(String token) {
		
		Claims claims = null;
		if (StringUtils.isNotBlank(token)) {
			claims = TokenUtils.getClaims(token);
			if (claims == null) {
				log.error("token已过期");
//				throw new TokenException("token已过期");
			}
		}
		return claims;
	}

	
	
}
