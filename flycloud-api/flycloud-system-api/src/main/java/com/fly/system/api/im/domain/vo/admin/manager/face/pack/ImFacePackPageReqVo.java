package com.fly.system.api.im.domain.vo.admin.manager.face.pack;

import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.common.domain.bo.PageBo;
import com.fly.system.api.im.validation.InEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.fly.system.api.im.constants.ImDateConstants.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - IM 表情包分页 Request VO")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ImFacePackPageReqVo extends PageBo {

    @Schema(description = "表情包名称，模糊匹配", example = "猫")
    private String name;

    @Schema(description = "状态", example = "0")
//    @InEnum(value = CommonStatusEnum.class, message = "状态必须是 {value}")
    private Integer status; // 参见 CommonStatusEnum 枚举类

    @Schema(description = "创建时间", example = "[2026-04-01 00:00:00, 2026-04-30 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
