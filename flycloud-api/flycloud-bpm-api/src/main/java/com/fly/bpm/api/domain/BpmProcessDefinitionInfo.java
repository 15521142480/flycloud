package com.fly.bpm.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.database.handler.StringListTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * BPM 流程定义的信息对象 bpm_process_definition_info
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bpm_process_definition_info", autoResultMap = true)
@Accessors(chain = true)
public class BpmProcessDefinitionInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 流程定义的编号
     * 关联 {@link org.flowable.engine.repository.ProcessDefinition#getId()} 属性
     */
    private String processDefinitionId;
    /**
     * 流程模型的编号
     */
    private String modelId;
    /**
     * 流程模型的类型
     */
    private Integer modelType;
    /**
     * 图标
     */
    private String icon;
    /**
     * 描述
     */
    private String description;
    /**
     * 表单类型
     */
    private Integer formType;
    /**
     * 表单编号
     */
    private Long formId;
    /**
     * 表单的配置
     */
    private String formConf;

    /**
     * 表单项的数组
     *
     * 在表单类型为 {@link com.fly.common.enums.bpm.BpmModelFormTypeEnum#NORMAL} 时
     *
     * 冗余 {@link BpmForm#getFields()}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> formFields;

    /**
     * 自定义表单的提交路径
     */
    private String formCustomCreatePath;
    /**
     * 自定义表单的查看路径
     */
    private String formCustomViewPath;
    /**
     * SIMPLE 设计器模型数据 JSON 格式
     */
    private String simpleModel;
    /**
     * 是否可见
     */
    private Integer visible;

    /**
     * 可发起用户编号数组
     *
     * 关联 {@link SysUser#getId()} 字段的数组
     *
     * 如果为空，则表示“全部可以发起”！
     *
     * 它和 {@link #visible} 的区别在于：
     * 1. {@link #visible} 只是决定是否可见。即使不可见，还是可以发起
     * 2. startUserIds 决定某个用户是否可以发起。如果该用户不可发起，则他也是不可见的
     */
    @TableField(typeHandler = StringListTypeHandler.class) // 为了可以使用 find_in_set 进行过滤
    private List<Long> startUserIds;

    /**
     * 可管理用户编号数组
     *
     * 关联 {@link SysUser#getId()} 字段的数组
     */
    @TableField(typeHandler = StringListTypeHandler.class) // 为了可以使用 find_in_set 进行过滤
    private List<Long> managerUserIds;

}
