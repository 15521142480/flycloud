package com.fly.common.feign.interceptor;

import cn.hutool.core.util.StrUtil;
import com.fly.common.config.properties.AuthProperties;
import com.fly.common.constant.CommonConstants;
import com.fly.common.utils.feign.FeignSignatureUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.UUID;

/**
 * feign请求拦截器
 *
 * note 一共处理两个问题，如：服务A调用服务B
 *      1:服务 B 怎么确认这个 /feign/** 请求真的是内部服务发来的，而不是外部用户直接调的。
 *      2:服务 B 需要知道当前登录用户是谁。
 *      注意：feign接口统一规范为：/feign/**
 *
 * @author: lxs
 * @date: 2025/8/13
 */
@RequiredArgsConstructor
public class FeignRequestInterceptor implements RequestInterceptor {


    private final AuthProperties authProperties;


    /**
     * 赋值token放在请求头
     *
     * @param requestTemplate 请求参数
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {

        String path = FeignSignatureUtils.normalizePath(requestTemplate.path());
        if (!FeignSignatureUtils.isFeignPath(path)) {
            return;
        }

        // 1、接口赋值token
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {

            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String token = request.getHeader(CommonConstants.AUTHORIZATION_KEY);
            if (StrUtil.isNotBlank(token)) {
                requestTemplate.header(CommonConstants.AUTHORIZATION_KEY, token);
            }
        }

        // 2、接口签名
        this.signFeignRequest(requestTemplate, path);
    }


    /**
     * 为Feign内部接口追加签名请求头。
     *
     * @param requestTemplate Feign请求模板
     */
    private void signFeignRequest(RequestTemplate requestTemplate, String path) {

        if (!authProperties.getFeign().isEnabled()) {
            return;
        }
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        String nonce = UUID.randomUUID().toString().replace("-", "");
        String signature = FeignSignatureUtils.sign(
                authProperties.getFeign().getSecret(),
                requestTemplate.method(),
                path,
                timestamp,
                nonce);
        requestTemplate.header(FeignSignatureUtils.HEADER_TIMESTAMP, timestamp);
        requestTemplate.header(FeignSignatureUtils.HEADER_NONCE, nonce);
        requestTemplate.header(FeignSignatureUtils.HEADER_SIGNATURE, signature);
    }

}
