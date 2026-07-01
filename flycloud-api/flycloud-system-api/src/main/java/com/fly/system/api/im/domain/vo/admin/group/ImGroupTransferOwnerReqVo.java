package com.fly.system.api.im.domain.vo.admin.group;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - 群主转让 Request VO")
@Data
@Accessors(chain = true)
public class ImGroupTransferOwnerReqVo {

    @Schema(description = "群编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13279")
    @NotNull(message = "群编号不能为空")
    private Long id;

    @Schema(description = "新群主用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "202")
    @NotNull(message = "新群主用户编号不能为空")
    private Long newOwnerUserId;

}
