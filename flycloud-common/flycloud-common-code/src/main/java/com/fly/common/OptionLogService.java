package com.fly.common;

import org.springframework.scheduling.annotation.Async;

/**
 * 操作日志 - 接口层
 *
 * @author: lxs
 * @date: 2024/9/4
 */
public interface OptionLogService {


    /**
     * 执行操作日志的记录
     */
//    @Async("xxx")
    void executeOptionLogRecord();


//    @Async("xxx")
    void executeOptionLogRecordBySystem();

    void executeOptionLogRecordByMall();
    void executeOptionLogRecordByMusic();

}
