package com.fly.mall.api.promotion.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 客服会话 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class KeFuConversationVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private LocalDateTime lastMessageTime;

    private String lastMessageContent;

    private Integer lastMessageContentType;

    private Boolean adminPinned;

    private Boolean userDeleted;

    private Boolean adminDeleted;

    private Integer adminUnreadMessageCount;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
