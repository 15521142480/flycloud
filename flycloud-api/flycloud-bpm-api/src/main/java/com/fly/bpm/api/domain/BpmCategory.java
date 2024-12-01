package com.fly.bpm.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * BPM 流程分类对象 bpm_category
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bpm_category")
public class BpmCategory extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 分类编号
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 分类名
     */
    private String name;
    /**
     * 分类标志
     */
    private String code;
    /**
     * 分类描述
     */
    private String description;
    /**
     * 分类状态
     */
    private Integer status;
    /**
     * 分类排序
     */
    private Long sort;

}
