package com.fly.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求网关的日志过滤器
 *
 * @author lxs
 * @date 2023/4/29
 */
@Configuration
@Slf4j
public class RequestLogFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String path = request.getPath().pathWithinApplication().value();
        String requestUrl = this.getOriginalRequestUrl(exchange);
        String method = request.getMethodValue();
        HttpHeaders headers = request.getHeaders();

        log.info("=====request-url:{}, method: {}", requestUrl, method);
        if ("POST".equals(method)) {

            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                String bodyString = new String(bytes, StandardCharsets.UTF_8);
                log.info("=====params:{}", formatStr(bodyString)); // formData
                exchange.getAttributes().put("POST_BODY", bodyString);
                DataBufferUtils.release(dataBuffer);
                Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    return Mono.just(buffer);
                });

                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(request) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return cachedFlux;
                    }
                };
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            });

        } else if ("GET".equals(method)) {

            MultiValueMap<String, String> queryParams = request.getQueryParams();
            log.info("=====params:{}", queryParams);
            return chain.filter(exchange);
        }


        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }


    /**
     * 获取原请求地址
     */
    private String getOriginalRequestUrl(ServerWebExchange exchange) {

        ServerHttpRequest req = exchange.getRequest();
        LinkedHashSet<URI> uris = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        URI requestUri = uris.stream().findFirst().orElse(req.getURI());
        MultiValueMap<String, String> queryParams = req.getQueryParams();

        // 第一种: /v3/api-docs/flycloud-gateway
//         return UriComponentsBuilder.fromPath(requestUri.getRawPath()).queryParams(queryParams).build().toUriString();

        // 第二种: http://localhost:8080/v3/api-docs/flycloud-gateway
        return requestUri.toString();
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
