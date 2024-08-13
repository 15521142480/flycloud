package com.fly.common.feign.config;

import com.fly.common.constant.CommonConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * feign客户端token拦截器
 *
 * <p>
 * 发送FeignClient设置Header信息等
 *
 * @author: lxs
 * @date: 2024/8/13
 */
@Component
public class TokenFeignClientInterceptor implements RequestInterceptor {


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
            requestTemplate.header(CommonConstants.AUTHORIZATION_KEY, token);
        }
    }

}
