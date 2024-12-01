package com.fly.bpm.api.domain.vo.task;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 流程任务的的分页 Request VO") // 待办、已办，都使用该分页
@Data
public class BpmTaskPageReqVO extends PageBo {

    @Schema(description = "流程任务名", example = "fly")
    private String name;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime[] createTime;

}
