package com.fly.bpm.api.domain.dto;

import lombok.Data;

/**
 * BPM 流程 MetaInfo Response DTO
 * 主要用于 { Model#setMetaInfo(String)} 的存储
 *
 * 最终，它的字段和 {@link cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmProcessDefinitionInfoDO} 是一致的
 *
 */
@Data
public class BpmModelMetaInfoRespDTO {

    /**
     * 流程图标
     */
    private String icon;
    /**
     * 流程描述
     */
    private String description;

    /**
     * 表单类型
     */
    private Integer formType;
    /**
     * 表单编号
     * 在表单类型为 {@link com.fly.common.enums.bpm.BpmModelFormTypeEnum#NORMAL} 时
     */
    private Long formId;
    /**
     * 自定义表单的提交路径，使用 Vue 的路由地址
     * 在表单类型为 {@link com.fly.common.enums.bpm.BpmModelFormTypeEnum#CUSTOM} 时
     */
    private String formCustomCreatePath;
    /**
     * 自定义表单的查看路径，使用 Vue 的路由地址
     * 在表单类型为 {@link com.fly.common.enums.bpm.BpmModelFormTypeEnum#CUSTOM} 时
     */
    private String formCustomViewPath;

}
