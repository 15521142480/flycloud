package com.fly.system.api.im.domain.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fly.system.api.im.enums.ImConversationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * IM 音视频通话业务对象。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Schema(description = "IM 音视频通话业务对象")
@Data
@Accessors(chain = true)
public class ImRtcCallBo {

    @Schema(description = "会话类型", example = "1")
    private Integer conversationType;

    @Schema(description = "媒体类型", example = "1")
    private Integer mediaType;

    @Schema(description = "群编号", example = "1024")
    private Long groupId;

    @Schema(description = "通话房间号", example = "room-xxx")
    private String room;

    @Schema(description = "被邀请用户编号列表", example = "[1, 2]")
    private Set<Long> inviteeIds;

    @AssertTrue(message = "会话类型仅支持单聊或群聊")
    @JsonIgnore
    public boolean isConversationTypeSupported() {
        return conversationType == null
                || ImConversationTypeEnum.PRIVATE.getType().equals(conversationType)
                || ImConversationTypeEnum.GROUP.getType().equals(conversationType);
    }

}
