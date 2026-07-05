package com.fly.common.utils.auth;

import com.fly.common.constant.CommonConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.Map;

/**
 * Token工具类
 *
 */
@Slf4j
public class TokenUtils {

	public static Integer AUTH_LENGTH = 7;

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


	/**
	 * 获取token串
	 *
	 * @param auth token
	 */
	public static String getToken(String auth) {

		if (auth == null || auth.length() <= AUTH_LENGTH) {
			return null;
		}

		// 只认Bearer
		String headStr = auth.substring(0, 6);
		if (headStr.compareToIgnoreCase(CommonConstants.AUTHORIZATION_BEARER) != 0) {
			return null;
		}

		return auth.substring(7);
	}


	/**
	 * 获取jwt中的claims信息
	 *
	 * @param token
	 */
	public static Claims getClaims(String token) {

		Claims claims = null;
		try {
			String[] parts = token.split("\\.");
			if (parts.length < 2) {
				return null;
			}
			byte[] payload = Base64.getUrlDecoder().decode(parts[1]);
			Map<String, Object> values = OBJECT_MAPPER.readValue(payload, new TypeReference<>() {
			});
			claims = Jwts.claims(values);
		} catch (Exception e) {
			log.error("解析token异常!", e);
		}

		return claims;
	}


}
