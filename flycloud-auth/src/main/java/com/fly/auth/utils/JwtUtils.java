package com.fly.auth.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fly.auth.domain.vo.AuthTokenClaimsVo;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.utils.json.JsonUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * jwt工具类
 *
 * @author: lxs
 * @date: 2026/7/5
 */
public class JwtUtils {



    /**
     * 构建token jwt信息
     *
     * @param tokenClaimsVo
     * @param duration 持续时间
    */
    public static TokenJwtInfo buildTokenJwt(AuthTokenClaimsVo tokenClaimsVo, Duration duration) {

        String accessId = UUID.randomUUID().toString();
        Instant now = Instant.now();
        Map<String, Object> claims = JsonUtils.parseObject(JsonUtils.toJsonString(tokenClaimsVo), new TypeReference<Map<String, Object>>() {});
        String token = Jwts.builder()
                .claims(claims)
                .id(accessId)
                .subject(String.valueOf(tokenClaimsVo.getUserName()))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(duration)))
                .signWith(signingKey())
                .compact();

        return new TokenJwtInfo(accessId, token);
    }





    /**
     * 签名密钥
     */
    public static SecretKey signingKey() {

        byte[] source = Oauth2Constants.SIGN_KEY.getBytes(StandardCharsets.UTF_8);
        byte[] key = new byte[32];
        for (int i = 0; i < key.length; i++) {
            key[i] = source[i % source.length];
        }
        return Keys.hmacShaKeyFor(key);
    }


    /**
     * token Jwt信息 - 精简类
     *
    */
    public record TokenJwtInfo(

            // 唯一编号
            String accessId,

            // token值
            String token
    ) {

    }

}
