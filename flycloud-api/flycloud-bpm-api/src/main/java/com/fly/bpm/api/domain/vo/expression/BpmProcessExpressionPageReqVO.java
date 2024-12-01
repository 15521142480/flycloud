package com.fly.bpm.api.domain.vo.expression;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.utils.DateUtils;
import com.fly.common.validate.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Schema(description = "管理后台 - BPM 流程表达式分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmProcessExpressionPageReqVO extends PageBo {

    @Schema(description = "表达式名字", example = "李四")
    private String name;

    @Schema(description = "表达式状态", example = "1")
    @InEnum(StatusEnum.class)
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime[] createTime;

}