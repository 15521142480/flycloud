package com.fly.bpm.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * BPM 流程达式对象 bpm_process_expression
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bpm_process_expression")
public class BpmProcessExpression extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 表达式名字
     */
    private String name;
    /**
     * 表达式状态
     */
    private Integer status;
    /**
     * 表达式
     */
    private String expression;

}
