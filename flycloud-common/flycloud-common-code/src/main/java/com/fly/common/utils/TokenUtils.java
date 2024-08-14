package com.fly.common.utils;

import com.fly.common.constant.CommonConstants;
import com.fly.common.constant.Oauth2Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Base64;

/**
 * Token工具类
 *
 */
public class TokenUtils {

	public static Integer AUTH_LENGTH = 7;


	/**
	 * 获取token串
	 *
	 * @param auth token
	 */
	public static String getToken(String auth) {

		if ((auth != null) && (auth.length() > AUTH_LENGTH)) {
			String headStr = auth.substring(0, 6);  // .toLowerCase();
			if (headStr.compareTo(CommonConstants.AUTHORIZATION_BEARER) == 0) { // bearer
				auth = auth.substring(7);
			}
			return auth;
		}
		return null;
	}


	/**
	 * 获取jwt中的claims信息
	 *
	 * @param token
	 */
	public static Claims getClaims(String token) {

		String key = Base64.getEncoder().encodeToString(Oauth2Constants.SIGN_KEY.getBytes());
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		return claims;
	}


}
