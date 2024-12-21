package com.fly.bpm.api.domain.bo;

import com.fly.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Schema(description = "管理后台 - 请假申请创建 Request VO")
@Data
public class BpmOALeaveCreateBo {

    @Schema(description = "请假的开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime startTime;

    @Schema(description = "请假的结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime endTime;

    @Schema(description = "请假类型-参见 bpm_oa_type 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "阅读芋道源码")
    private String reason;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;

    @AssertTrue(message = "结束时间，需要在开始时间之后")
    public boolean isEndTimeValid() {
        return !getEndTime().isBefore(getStartTime());
    }

}
