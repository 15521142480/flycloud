package com.fly.bpm.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

import java.util.Set;

/**
 * BPM 用户组对象 bpm_user_group
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bpm_user_group")
public class BpmUserGroup extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 组名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 成员用户编号数组
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Long> userIds;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

}
