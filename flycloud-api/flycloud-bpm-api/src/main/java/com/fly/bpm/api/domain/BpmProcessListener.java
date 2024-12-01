package com.fly.bpm.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * BPM 流程监听器对象 bpm_process_listener
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bpm_process_listener")
public class BpmProcessListener extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 监听器名字
     */
    private String name;
    /**
     * 监听器类型
     */
    private String type;
    /**
     * 监听器状态
     */
    private Integer status;
    /**
     * 监听事件
     */
    private String event;
    /**
     * 监听器值类型
     */
    private String valueType;
    /**
     * 监听器值
     */
    private String value;

}
