package com.fly.system.api.im.domain.vo.admin.rtc;

import com.fly.system.api.im.validation.InEnum;
import com.fly.system.api.im.enums.ImConversationTypeEnum;
import com.fly.system.api.im.enums.rtc.ImRtcCallMediaTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Schema(description = "管理后台 - 创建通话 Request VO")
@Data
@Accessors(chain = true)
public class ImRtcCallCreateReqVo {

    @Schema(description = "会话类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "会话类型不能为空")
//    @InEnum(ImConversationTypeEnum.class)
    private Integer conversationType;

    @Schema(description = "媒体类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "媒体类型不能为空")
//    @InEnum(ImRtcCallMediaTypeEnum.class)
    private Integer mediaType;

    @Schema(description = "群编号；群聊场景必填", example = "2048")
    private Long groupId;

    @Schema(description = "被邀请的用户编号集合；私聊必传 1 个对端，群聊必传至少 1 人")
    private Set<@NotNull(message = "被邀请用户编号不能为空") Long> inviteeIds;

    /**
     * 通话仅支持私聊和群聊
     */
    @AssertTrue(message = "会话类型不支持")
    @JsonIgnore
    public boolean isConversationTypeSupported() {
        return conversationType == null || ImConversationTypeEnum.isPrivate(conversationType)
                || ImConversationTypeEnum.isGroup(conversationType);
    }

}
