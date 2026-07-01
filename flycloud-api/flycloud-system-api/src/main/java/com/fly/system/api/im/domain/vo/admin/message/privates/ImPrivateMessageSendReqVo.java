package com.fly.system.api.im.domain.vo.admin.message.privates;

import com.fly.system.api.im.validation.InEnum;
import com.fly.system.api.im.enums.message.ImMessageTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 私聊消息发送 Request VO
 */
@Schema(description = "管理后台 - 私聊消息发送 Request VO")
@Data
@Accessors(chain = true)
public class ImPrivateMessageSendReqVo {

    @Schema(description = "客户端消息编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "uuid-xxx")
    @NotEmpty(message = "客户端消息编号不能为空")
    private String clientMessageId;

    @Schema(description = "接收人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "接收人编号不能为空")
    private Long receiverId;

    @Schema(description = "消息类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "消息类型不能为空")
    @InEnum(ImMessageTypeEnum.class)
    private Integer type;

    @Schema(description = "消息内容，JSON 格式", requiredMode = Schema.RequiredMode.REQUIRED, example = "{\"content\":\"你好\"}")
    @NotEmpty(message = "消息内容不能为空")
    private String content;

    /**
     * 仅允许用户消息（normal）类型
     */
    @AssertTrue(message = "消息类型不允许")
    @JsonIgnore
    public boolean isTypeNormal() {
        return type == null || ImMessageTypeEnum.validate(type).isNormal();
    }

}
