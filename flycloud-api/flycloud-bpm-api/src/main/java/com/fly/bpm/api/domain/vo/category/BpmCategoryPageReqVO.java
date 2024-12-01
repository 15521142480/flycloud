package com.fly.bpm.api.domain.vo.category;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.utils.DateUtils;
import com.fly.common.validate.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Schema(description = "管理后台 - BPM 流程分类分页 Request VO")
@Data
public class BpmCategoryPageReqVO extends PageBo {

    @Schema(description = "分类名", example = "王五")
    private String name;

    @Schema(description = "分类标志", example = "OA")
    private String code;

    @Schema(description = "分类状态", example = "1")
    @InEnum(StatusEnum.class)
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime[] createTime;

}