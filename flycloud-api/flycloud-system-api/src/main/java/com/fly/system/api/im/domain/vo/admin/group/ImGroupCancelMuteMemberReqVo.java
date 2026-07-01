package com.fly.system.api.im.domain.vo.admin.group;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - 取消成员禁言 Request VO")
@Data
@Accessors(chain = true)
public class ImGroupCancelMuteMemberReqVo {

    @Schema(description = "群编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "群编号不能为空")
    private Long id;

    @Schema(description = "被取消禁言的用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

}
