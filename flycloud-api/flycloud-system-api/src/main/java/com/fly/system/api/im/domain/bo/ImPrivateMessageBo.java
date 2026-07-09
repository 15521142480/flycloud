package com.fly.system.api.im.domain.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fly.system.api.im.enums.message.ImMessageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM 私聊消息业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 私聊消息业务对象")
@Data
@Accessors(chain = true)
public class ImPrivateMessageBo {

    @Schema(description = "客户端消息编号", example = "uuid-xxx")
    private String clientMessageId;

    @Schema(description = "接收人编号", example = "1")
    private Long receiverId;

    @Schema(description = "起始消息编号，从该 id 往前拉取（不含）。为空则从最新消息开始", example = "1024")
    private Long maxId;

    @Schema(description = "拉取数量", example = "20")
    private Integer limit;

    @Schema(description = "消息类型", example = "0")
    private Integer type;

    @Schema(description = "消息内容，JSON 格式", example = "{\"content\":\"你好\"}")
    private String content;

    @AssertTrue(message = "消息类型不允许")
    @JsonIgnore
    public boolean isTypeNormal() {
        return type == null || ImMessageTypeEnum.validate(type).isNormal();
    }

}
