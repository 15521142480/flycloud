package com.fly.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.fly.common.domain.model.R;
import com.fly.common.utils.json.JsonUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 响应工具
 *
 */
public class ResponseUtils {

	/**
	 * 设置响应
	 *
	 * @param response    HttpServletResponse
	 * @param contentType content-type
	 * @param status      http状态码
	 * @param value       响应内容
	 * @throws IOException IOException
	 */
	public static void responseWriter(HttpServletResponse response, String contentType,
	                                  int status, Object value) throws IOException {
		response.setContentType(contentType);
		response.setStatus(status);
		response.getOutputStream().write(JSONObject.toJSONString(value).getBytes());
	}

	/**
	 * 设置webflux模型响应
	 *
	 * @param response    ServerHttpResponse
	 * @param contentType content-type
	 * @param status      http状态码
	 * @param value       响应内容
	 * @return Mono<Void>
	 */
	public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, HttpStatus status, String value) {
		response.setStatusCode(status);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
		String result = JsonUtils.toJsonString(R.failed(value));
		byte[] bytes = result.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = response.bufferFactory().wrap(bytes);
		return response.writeWith(Mono.just(buffer));
	}


}
