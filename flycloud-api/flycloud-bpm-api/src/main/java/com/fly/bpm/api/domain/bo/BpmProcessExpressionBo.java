package com.fly.bpm.api.domain.bo;

import com.fly.common.utils.DateUtils;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * BPM 流程达式业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
public class BpmProcessExpressionBo {

    /**
     * 编号
     */
    // @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 表达式名字
     */
    // @NotBlank(message = "表达式名字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 表达式状态
     */
    // @NotNull(message = "表达式状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 是否删除
     */
    private Boolean isDeleted;

    /**
     * 表达式
     */
    // @NotBlank(message = "表达式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String expression;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime[] createTime;

}
