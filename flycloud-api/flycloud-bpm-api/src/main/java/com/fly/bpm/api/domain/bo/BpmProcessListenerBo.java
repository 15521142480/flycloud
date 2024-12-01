package com.fly.bpm.api.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * BPM 流程监听器业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BpmProcessListenerBo extends BaseEntity {

    /**
     * 编号
     */
    // @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 监听器名字
     */
    // @NotBlank(message = "监听器名字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 监听器类型
     */
    // @NotBlank(message = "监听器类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 监听器状态
     */
    // @NotNull(message = "监听器状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 监听事件
     */
    // @NotBlank(message = "监听事件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String event;

    /**
     * 监听器值类型
     */
    // @NotBlank(message = "监听器值类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String valueType;

    /**
     * 监听器值
     */
    // @NotBlank(message = "监听器值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String value;


}
