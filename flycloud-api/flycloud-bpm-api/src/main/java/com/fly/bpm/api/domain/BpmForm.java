package com.fly.bpm.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import com.fly.common.domain.BaseEntity;

/**
 * BPM 单定义对象 bpm_form
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bpm_form", autoResultMap = true)
public class BpmForm extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 表单名
     */
    private String name;
    /**
     * 开启状态
     */
    private Integer status;
    /**
     * 表单的配置
     */
    private String conf;

//    /**
//     * 表单项的数组
//     */
//    private String fields;

    /**
     * 表单项的数组
     *
     * 目前直接将 https://github.com/JakHuang/form-generator 生成的 JSON 串，直接保存
     * 定义：https://github.com/JakHuang/form-generator/issues/46
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> fields;

    /**
     * 备注
     */
    private String remark;

}
