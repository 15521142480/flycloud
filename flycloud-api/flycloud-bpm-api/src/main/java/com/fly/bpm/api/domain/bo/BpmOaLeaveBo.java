package com.fly.bpm.api.domain.bo;

import com.fly.common.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

import com.fly.common.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * OA 请假申请业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BpmOaLeaveBo extends BaseEntity {

    /**
     * 请假表单主键
     */
    // @NotNull(message = "请假表单主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 申请人的用户编号
     */
    // @NotNull(message = "申请人的用户编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 请假类型
     */
    // @NotNull(message = "请假类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer type;

    /**
     * 请假原因
     */
    // @NotBlank(message = "请假原因不能为空", groups = { AddGroup.class, EditGroup.class })
    private String reason;

    /**
     * 开始时间
     */
    // @NotNull(message = "开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    // @NotNull(message = "结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime endTime;

    /**
     * 请假天数
     */
    // @NotNull(message = "请假天数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer day;

    /**
     * 审批结果
     */
    // @NotNull(message = "审批结果不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 流程实例的编号
     */
    // @NotBlank(message = "流程实例的编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String processInstanceId;


}
