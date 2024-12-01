package com.fly.bpm.api.domain.vo.instance;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Schema(description = "管理后台 - 流程实例抄送的分页 Request VO")
public class BpmProcessInstanceCopyPageReqVO extends PageBo {

    @Schema(description = "流程名称", example = "fly")
    private String processInstanceName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime[] createTime;

}
