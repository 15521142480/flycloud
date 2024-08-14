package com.fly.gateway.filter;

import com.fly.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网关日志过滤器
 *
 * @author: lxs
 * @date: 2024/8/14
 */
@Slf4j
@Component
@AllArgsConstructor
public class RequestLogFilter implements GlobalFilter, Ordered {

    
    private static final String START_TIME = "startTime";

    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // todo 1 请求地址 （分为原请求地址和转发后的地址）
        String originalRequestUrl = this.getOriginalRequestUrl(exchange);
        String tranRequest = this.getTranRequestUrl(exchange);

        // 构建成一条长 日志，避免并发下日志错乱
        StringBuilder beforeReqLog = new StringBuilder(300);
        // 日志参数
        List<Object> beforeReqArgs = new ArrayList<>();

        beforeReqLog.append("\n\n================  Gateway Request Start  ================\n");
        // 打印路由
        beforeReqLog.append("===> {}: {}\n");

        // 参数
        String requestMethod = exchange.getRequest().getMethodValue();
        beforeReqArgs.add(requestMethod);
        beforeReqArgs.add(originalRequestUrl);

        // 打印请求头
        HttpHeaders headers = exchange.getRequest().getHeaders();
        headers.forEach((headerName, headerValue) -> {
            beforeReqLog.append("===Headers===  {}: {}\n");
            beforeReqArgs.add(headerName);
            beforeReqArgs.add(StringUtils.join(headerValue));
        });

        beforeReqLog.append("================  Gateway Request End  =================\n");
        // 打印执行时间
        log.info(beforeReqLog.toString(), beforeReqArgs.toArray());

        // todo 2 响应
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {

            ServerHttpResponse response = exchange.getResponse();
            Long startTime = exchange.getAttribute(START_TIME);
            long executeTime = 0L;
            if (startTime != null) {
                executeTime = (System.currentTimeMillis() - startTime);
            }

            // 构建成一条长 日志，避免并发下日志错乱
            StringBuilder responseLog = new StringBuilder(300);
            // 日志参数
            List<Object> responseArgs = new ArrayList<>();

            responseLog.append("\n\n================  Gateway Response Start  ================\n");
            // 打印路由 200 get: /fly*/xxx/xxx
            responseLog.append("<=== {} {}: {}: {}\n");

            // 参数
            responseArgs.add(response.getStatusCode().value());
            responseArgs.add(tranRequest);
            responseArgs.add(requestMethod);
            responseArgs.add(executeTime + "ms");

            // 打印请求头
            HttpHeaders httpHeaders = response.getHeaders();
            httpHeaders.forEach((headerName, headerValue) -> {
                responseLog.append("===Headers===  {}: {}\n");
                responseArgs.add(headerName);
                responseArgs.add(StringUtils.join(headerValue));
            });

            responseLog.append("================   Gateway Response End  =================\n");
            // 打印执行时间
            log.info(responseLog.toString(), responseArgs.toArray());
        }));

//        if ("GET".equals(method)) {
//
//            MultiValueMap<String, String> queryParams = request.getQueryParams();
//            log.info("=====params:{}", queryParams);
//            return chain.filter(exchange);
//        }
    }


    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }


    // ==========================================================================

    /**
     * 获取原请求地址
     */
    private String getOriginalRequestUrl(ServerWebExchange exchange) {

        ServerHttpRequest req = exchange.getRequest();
        LinkedHashSet<URI> uris = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        URI requestUri = uris.stream().findFirst().orElse(req.getURI());

        // 第一种: /v3/api-docs/flycloud-gateway
//        MultiValueMap<String, String> queryParams = req.getQueryParams();
//        return UriComponentsBuilder.fromPath(requestUri.getRawPath()).queryParams(queryParams).build().toUriString();

        // 第二种: http://localhost:8080/v3/api-docs/flycloud-gateway
        return requestUri.toString();
    }


    /**
     * 获取转发后的地址
     */
    private String getTranRequestUrl(ServerWebExchange exchange) {

        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        // 配置文件中配置的route的uri属性(匹配到的route)，即断言代理后的地址
        String uri = route.getUri().toString();

        // 请求路径中域名之后的部分,本例中是/api/name/get
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();

        return uri + path;
    }


    /**
     * 去掉FormData 空格,换行和制表符
     *
     * @param str
     */
    private static String formatStr(String str) {

        if (str != null && str.length() > 0) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            return m.replaceAll("");
        }
        return str;
    }
    
}
