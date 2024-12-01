package com.fly.bpm.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * BPM 流程定义的信息视图对象
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@ExcelIgnoreUnannotated
public class BpmProcessDefinitionInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 流程定义的编号
     */
    @ExcelProperty(value = "流程定义的编号")
    private String processDefinitionId;

    /**
     * 流程模型的编号
     */
    @ExcelProperty(value = "流程模型的编号")
    private String modelId;

    /**
     * 流程模型的类型
     */
    @ExcelProperty(value = "流程模型的类型")
    private Integer modelType;

    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    private String icon;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;

    /**
     * 表单类型
     */
    @ExcelProperty(value = "表单类型")
    private Integer formType;

    /**
     * 表单编号
     */
    @ExcelProperty(value = "表单编号")
    private Long formId;

    /**
     * 表单的配置
     */
    @ExcelProperty(value = "表单的配置")
    private String formConf;

    /**
     * 表单项的数组
     */
    @ExcelProperty(value = "表单项的数组")
    private String formFields;

    /**
     * 自定义表单的提交路径
     */
    @ExcelProperty(value = "自定义表单的提交路径")
    private String formCustomCreatePath;

    /**
     * 自定义表单的查看路径
     */
    @ExcelProperty(value = "自定义表单的查看路径")
    private String formCustomViewPath;

    /**
     * SIMPLE 设计器模型数据 JSON 格式
     */
    @ExcelProperty(value = "SIMPLE 设计器模型数据 JSON 格式")
    private String simpleModel;

    /**
     * 是否可见
     */
    @ExcelProperty(value = "是否可见")
    private Integer visible;

    /**
     * 可发起用户编号数组
     */
    @ExcelProperty(value = "可发起用户编号数组")
    private String startUserIds;

    /**
     * 可管理用户编号数组
     */
    @ExcelProperty(value = "可管理用户编号数组")
    private String managerUserIds;

    /**
     * 是否删除
     */
    @ExcelProperty(value = "是否删除")
    private Integer isDeleted;


}
