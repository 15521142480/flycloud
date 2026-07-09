package com.fly.common.feign.config;

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
 * feign客户端token拦截器
 *
 * <p>
 * 发送FeignClient设置Header信息等
 *
 * @author: lxs
 * @date: 2025/8/13
 */
@RequiredArgsConstructor
public class TokenFeignClientInterceptor implements RequestInterceptor {

    private final AuthProperties authProperties;

    /**
     * 赋值token放在请求头
     *
     * @param requestTemplate 请求参数
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {

//        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {

            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

//            String url = request.getRequestURI();
//            String userName = request.getParameter("username");
//            System.out.println("===url " + url);
//            System.out.println("===username " + userName);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

            String token = request.getHeader(CommonConstants.AUTHORIZATION_KEY);
            if (StrUtil.isNotBlank(token)) {
                requestTemplate.header(CommonConstants.AUTHORIZATION_KEY, token);
            }
        }

        signFeignRequest(requestTemplate);
    }

    /**
     * 为Feign内部接口追加签名请求头。
     *
     * @param requestTemplate Feign请求模板
     */
    private void signFeignRequest(RequestTemplate requestTemplate) {
        String path = FeignSignatureUtils.normalizePath(requestTemplate.path());
        if (!authProperties.getFeign().isEnabled() || !FeignSignatureUtils.isFeignPath(path)) {
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
