package com.fly.im.controller.admin.manager.friend.vo;

import com.fly.common.domain.bo.PageBo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.fly.im.framework.util.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - IM 好友关系分页 Request VO")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ImFriendManagerPageReqVo extends PageBo {

    @Schema(description = "用户编号", example = "1024")
    private Long userId;

    @Schema(description = "好友用户编号", example = "2048")
    private Long friendUserId;

    @Schema(description = "好友状态", example = "0")
    private Integer status; // 参见 CommonStatusEnum 枚举类

    @Schema(description = "是否免打扰", example = "false")
    private Boolean silent;

    @Schema(description = "添加好友时间", example = "[2026-04-01 00:00:00, 2026-04-30 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] addTime;

}
