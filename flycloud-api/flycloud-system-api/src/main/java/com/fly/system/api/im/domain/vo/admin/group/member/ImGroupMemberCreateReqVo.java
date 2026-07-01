package com.fly.system.api.im.domain.vo.admin.group.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - 群成员邀请 Request VO")
@Data
@Accessors(chain = true)
public class ImGroupMemberCreateReqVo {

    @Schema(description = "群编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13279")
    @NotNull(message = "群编号不能为空")
    private Long groupId;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21730")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

}
