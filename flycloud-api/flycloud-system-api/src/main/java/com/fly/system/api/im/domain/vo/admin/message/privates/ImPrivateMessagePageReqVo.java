package com.fly.system.api.im.domain.vo.admin.message.privates;

import com.fly.common.domain.bo.PageBo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 私聊消息分页 Request VO")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ImPrivateMessagePageReqVo extends PageBo {

    @Schema(description = "接收人编号（对方）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "接收人编号不能为空")
    private Long receiverId;

}
