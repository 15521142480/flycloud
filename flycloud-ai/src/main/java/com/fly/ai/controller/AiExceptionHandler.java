package com.fly.ai.controller;

import com.fly.common.domain.model.R;
import com.fly.common.exception.AiProviderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * AI 服务全局异常处理。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Slf4j
@Order(-1)
@RestControllerAdvice(basePackages = "com.fly.ai")
public class AiExceptionHandler {

    /**
     * 将模型供应商异常转换为统一的业务响应，并保留错误日志用于排查。
     *
     * @param exception 模型供应商异常
     * @return 统一错误响应
     */
    @ExceptionHandler(AiProviderException.class)
    public ResponseEntity<R<Void>> handleProviderException(AiProviderException exception) {
        HttpStatus status = exception.getStatusCode() == 401 || exception.getStatusCode() == 403
                ? HttpStatus.BAD_GATEWAY
                : HttpStatus.resolve(exception.getStatusCode());
        if (status == null || status.is2xxSuccessful()) {
            status = HttpStatus.BAD_GATEWAY;
        }
        log.error("AI 调用失败，statusCode={}, message={}", exception.getStatusCode(), exception.getMessage(), exception);
        return ResponseEntity.status(status).body(R.failed("AI 服务调用失败：" + exception.getMessage()));
    }

}
