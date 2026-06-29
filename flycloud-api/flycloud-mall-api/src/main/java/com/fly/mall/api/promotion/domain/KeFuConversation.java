package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客服会话 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_kefu_conversation")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "客服会话对象", description = "客服会话表")
public class KeFuConversation extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private LocalDateTime lastMessageTime;

    private String lastMessageContent;

    private Integer lastMessageContentType;

    private Boolean adminPinned;

    private Boolean userDeleted;

    private Boolean adminDeleted;

    private Integer adminUnreadMessageCount;

}
