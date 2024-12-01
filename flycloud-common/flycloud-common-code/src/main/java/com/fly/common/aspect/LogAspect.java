package com.fly.common.aspect;

import com.fly.common.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 操作日志记录 - 切面
 *
 * @author: lxs
 * @date: 2024/9/4
 */
@Slf4j
@Aspect
@Component
public class LogAspect {


    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {

        this.handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {

        this.handleLog(joinPoint, controllerLog, e, null);
    }



    /**
     * 处理操作日志
     *
     * @param joinPoint
     * @param controllerLog
     * @param e
     * @param jsonResult
    */
    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {

            // *========数据库日志=========*//

//            OptionLogBo logBo = new OptionLogBo();
//
//            logBo.setxxx(xxx);

            // 1. 可以存在一个库；
            // 2. 或者可以存在不同库，方案：根据切面参数获取系统类型或者根据配置文件获取系统类型来确定不同库的日志表，然后不同服务执行不同接口

//            SpringUtils.getBean(OptionLogService.class).executeOptionLogRecord(logBo);


        } catch (Exception exp) {

            log.error("==记录日志异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }


}
