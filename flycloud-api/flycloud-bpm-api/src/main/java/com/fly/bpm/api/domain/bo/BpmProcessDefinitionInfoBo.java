package com.fly.bpm.api.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fly.common.domain.BaseEntity;

/**
 * BPM 流程定义的信息业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BpmProcessDefinitionInfoBo extends BaseEntity {

    /**
     * 编号
     */
    // @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 流程定义的编号
     */
    // @NotBlank(message = "流程定义的编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String processDefinitionId;

    /**
     * 流程模型的编号
     */
    // @NotBlank(message = "流程模型的编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String modelId;

    /**
     * 流程模型的类型
     */
    // @NotNull(message = "流程模型的类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer modelType;

    /**
     * 图标
     */
    // @NotBlank(message = "图标不能为空", groups = { AddGroup.class, EditGroup.class })
    private String icon;

    /**
     * 描述
     */
    // @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;

    /**
     * 表单类型
     */
    // @NotNull(message = "表单类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer formType;

    /**
     * 表单编号
     */
    // @NotNull(message = "表单编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long formId;

    /**
     * 表单的配置
     */
    // @NotBlank(message = "表单的配置不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formConf;

    /**
     * 表单项的数组
     */
    // @NotBlank(message = "表单项的数组不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formFields;

    /**
     * 自定义表单的提交路径
     */
    // @NotBlank(message = "自定义表单的提交路径不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formCustomCreatePath;

    /**
     * 自定义表单的查看路径
     */
    // @NotBlank(message = "自定义表单的查看路径不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formCustomViewPath;

    /**
     * SIMPLE 设计器模型数据 JSON 格式
     */
    // @NotBlank(message = "SIMPLE 设计器模型数据 JSON 格式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String simpleModel;

    /**
     * 是否可见
     */
    // @NotNull(message = "是否可见不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer visible;

    /**
     * 可发起用户编号数组
     */
    // @NotBlank(message = "可发起用户编号数组不能为空", groups = { AddGroup.class, EditGroup.class })
    private String startUserIds;

    /**
     * 可管理用户编号数组
     */
    // @NotBlank(message = "可管理用户编号数组不能为空", groups = { AddGroup.class, EditGroup.class })
    private String managerUserIds;


}
