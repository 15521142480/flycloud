package com.fly.bpm.api.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import com.fly.common.domain.BaseEntity;
import com.fly.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;



/**
 * BPM 流程实例抄送视图对象
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@ExcelIgnoreUnannotated
@Accessors(chain = true)
public class BpmProcessInstanceCopyVo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ExcelProperty(value = "编号")
    private Long id;

    /**
     * 用户编号，被抄送人
     */
    @ExcelProperty(value = "用户编号，被抄送人")
    private Long userId;

    /**
     * 发起流程的用户编号
     */
    @ExcelProperty(value = "发起流程的用户编号")
    private Long startUserId;

    /**
     * 流程实例的id
     */
    @ExcelProperty(value = "流程实例的id")
    private String processInstanceId;

    /**
     * 流程实例的名字
     */
    @ExcelProperty(value = "流程实例的名字")
    private String processInstanceName;

    /**
     * 流程定义的分类
     */
    @ExcelProperty(value = "流程定义的分类")
    private String category;

    /**
     * 发起抄送的任务编号
     */
    @ExcelProperty(value = "发起抄送的任务编号")
    private String taskId;

    /**
     * 任务的名字
     */
    @ExcelProperty(value = "任务的名字")
    private String taskName;

    /**
     * 流程活动编号
     */
    @ExcelProperty(value = "流程活动编号")
    private String activityId;


}
