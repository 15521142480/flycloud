package com.fly.mall.api.domain.promotion.vo;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 客服消息 视图对象。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class KeFuMessageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long conversationId;

    private Long senderId;

    private Integer senderType;

    private Long receiverId;

    private Integer receiverType;

    private Integer contentType;

    private String content;

    private Boolean readStatus;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
