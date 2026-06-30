package com.fly.im.controller.admin.manager.group.vo;

import com.fly.common.domain.bo.PageBo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.fly.im.framework.util.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - IM 群聊分页 Request VO")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ImGroupManagerPageReqVO extends PageBo {

    @Schema(description = "群名称，模糊匹配", example = "技术交流群")
    private String name;

    @Schema(description = "群主用户编号", example = "1024")
    private Long ownerUserId;

    @Schema(description = "群状态", example = "0")
    private Integer status; // 参见 CommonStatusEnum 枚举类（0 正常 / 1 已解散）

    @Schema(description = "是否封禁", example = "false")
    private Boolean banned;

    @Schema(description = "创建时间", example = "[2026-04-01 00:00:00, 2026-04-30 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
