package com.fly.bpm.api.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * BPM 流程达式业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BpmProcessExpressionBo extends BaseEntity {

    /**
     * 编号
     */
    // @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 表达式名字
     */
    // @NotBlank(message = "表达式名字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 表达式状态
     */
    // @NotNull(message = "表达式状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 表达式
     */
    // @NotBlank(message = "表达式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String expression;


}
