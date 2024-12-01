package com.fly.gateway.filter;

import com.fly.common.constant.CommonConstants;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.constant.WebConstants;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.utils.ResponseUtils;
import com.fly.common.utils.auth.SecurityUtils;
import com.fly.common.utils.StringPoolUtils;
import com.fly.common.utils.auth.TokenUtils;
import com.fly.gateway.config.properties.GatewayServerSecurityProperties;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 统一网关的token验证
 *
 * @author: lxs
 * @date: 2024/8/14
 */
@Slf4j
@Component
@AllArgsConstructor
public class TokenSecurityFilter implements GlobalFilter, Ordered {


    private final GatewayServerSecurityProperties gatewayServerSecurityProperties;
    private final RedisUtils redisUtils;

    /**
     * 服务前缀以/flycloud开头，如flycloud-system
     */
    public static final String PATH_PREFIX = "/flycloud";

    /**
     * 索引自1开头检索，跳过第一个字符就是检索的字符的问题
     */
    public static final int FROM_INDEX = 1;



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpResponse response = exchange.getResponse();

        // 如果未启用网关验证，则跳过
        if (!gatewayServerSecurityProperties.getEnable()) {
            return chain.filter(exchange);
        }

        //　过滤白名单
        String serverPath = this.replacePrefix(exchange.getRequest().getURI().getPath());
        String requestUrl = exchange.getRequest().getURI().getRawPath();
        if (this.ignore(serverPath) || this.ignore(requestUrl)) {
            return chain.filter(exchange);
        }

        // 验证token是否有效
        String headerToken = exchange.getRequest().getHeaders().getFirst(Oauth2Constants.HEADER_TOKEN_KEY);
        if (headerToken == null) {
            log.error("没有携带Token信息！");
            return unAuthorized(response, "没有携带Token信息！");
        }

        String token = TokenUtils.getToken(headerToken);
        Claims claims = SecurityUtils.getClaims(token);
        if (claims == null) {
            return this.unAuthorized(response, "token已过期！");
        }

        // 判断token是否存在于redis,对于只允许一台设备场景适用。
        // 如只允许一台设备登录，需要在登录成功后，查询key是否存在，如存在，则删除此key，提供思路。
        boolean hasKey = redisUtils.hasKey("auth:" + token);
        log.debug("查询token是否存在: {}", hasKey);
        if (!hasKey) {
            return unAuthorized(response, "登录超时，请重新登录");
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return CommonConstants.OAUTH_FILTER_ORDER;
    }



    // ========================================================

    /**
     * 检查是否忽略url
     * @param path 路径
     * @return boolean
     */
    private boolean ignore(String path) {

        return gatewayServerSecurityProperties.getIgnoreUrls().stream()
                .map(url -> url.replace("/**", ""))
                .anyMatch(path::startsWith);
    }

    /**
     * 排除本系统模块前缀
     *
     * @param path 路径
     */
    private String replacePrefix(String path) {

        if (path.startsWith(PATH_PREFIX)) {
            return path.substring(path.indexOf(StringPoolUtils.SLASH, FROM_INDEX));
        }
        return path;
    }


    /**
     * 返回授权错误信息
     */
    private Mono<Void> unAuthorized(ServerHttpResponse resp, String msg) {

        return ResponseUtils.webFluxResponseWriter(resp, WebConstants.JSON_UTF8, HttpStatus.UNAUTHORIZED, msg);
    }



}
