package com.fly.mall.api.domain.promotion.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 客服消息 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class KeFuMessageBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "conversationId")
    private Long conversationId;

    @Schema(description = "senderId")
    private Long senderId;

    @Schema(description = "senderType")
    private Integer senderType;

    @Schema(description = "receiverId")
    private Long receiverId;

    @Schema(description = "receiverType")
    private Integer receiverType;

    @Schema(description = "contentType")
    private Integer contentType;

    @Schema(description = "content")
    private String content;

    @Schema(description = "readStatus")
    private Boolean readStatus;

}
