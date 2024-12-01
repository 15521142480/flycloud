package com.fly.bpm.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

import com.fly.common.domain.BaseEntity;

/**
 * OA 请假申请对象 bpm_oa_leave
 *
 * @author fly
 * @date 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bpm_oa_leave")
public class BpmOaLeave extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 请假表单主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 申请人的用户编号
     */
    private Long userId;
    /**
     * 请假类型
     */
    private Integer type;
    /**
     * 请假原因
     */
    private String reason;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 请假天数
     */
    private Integer day;
    /**
     * 审批结果
     */
    private Integer status;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;

}
