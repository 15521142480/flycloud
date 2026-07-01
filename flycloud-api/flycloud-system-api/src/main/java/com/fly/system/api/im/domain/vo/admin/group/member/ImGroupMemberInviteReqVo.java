package com.fly.system.api.im.domain.vo.admin.group.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Schema(description = "管理后台 - 群成员邀请 Request VO")
@Data
@Accessors(chain = true)
public class ImGroupMemberInviteReqVo {

    @Schema(description = "群编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13279")
    @NotNull(message = "群编号不能为空")
    private Long groupId;

    @Schema(description = "被邀请的用户编号列表", requiredMode = Schema.RequiredMode.REQUIRED, example = "[1, 2, 3]")
    @NotEmpty(message = "被邀请的用户编号列表不能为空")
    private List<Long> memberUserIds;

}
