package com.fly.bpm.api.domain.vo;

import java.io.Serializable;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fly.common.annotation.ExcelDictFormat;
import com.fly.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * OA 请假申请视图对象
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@ExcelIgnoreUnannotated
public class BpmOaLeaveVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请假表单主键
     */
    @ExcelProperty(value = "请假表单主键")
    private Long id;

    /**
     * 申请人的用户编号
     */
    @ExcelProperty(value = "申请人的用户编号")
    private Long userId;

    /**
     * 请假类型
     */
    @ExcelProperty(value = "请假类型")
    private Integer type;

    /**
     * 请假原因
     */
    @ExcelProperty(value = "请假原因")
    private String reason;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 请假天数
     */
    @ExcelProperty(value = "请假天数")
    private Integer day;

    /**
     * 审批结果
     */
    @ExcelProperty(value = "审批结果")
    private Integer status;

    /**
     * 流程实例的编号
     */
    @ExcelProperty(value = "流程实例的编号")
    private String processInstanceId;

    /**
     * 是否删除
     */
    @ExcelProperty(value = "是否删除")
    private Integer isDeleted;


}
