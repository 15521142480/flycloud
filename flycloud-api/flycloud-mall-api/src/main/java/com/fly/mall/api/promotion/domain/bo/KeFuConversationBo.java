package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 客服会话 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class KeFuConversationBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "lastMessageTime")
    private LocalDateTime lastMessageTime;

    @Schema(description = "lastMessageContent")
    private String lastMessageContent;

    @Schema(description = "lastMessageContentType")
    private Integer lastMessageContentType;

    @Schema(description = "adminPinned")
    private Boolean adminPinned;

    @Schema(description = "userDeleted")
    private Boolean userDeleted;

    @Schema(description = "adminDeleted")
    private Boolean adminDeleted;

    @Schema(description = "adminUnreadMessageCount")
    private Integer adminUnreadMessageCount;

}
