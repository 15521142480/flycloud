package com.fly.common.enums.bpm;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BPM 流程监听器的类型
 *
 */
@Getter
@AllArgsConstructor
public enum BpmProcessListenerType {

    EXECUTION("execution", "执行监听器"),
    TASK("task", "任务执行器");

    private final String type;
    private final String name;

}
