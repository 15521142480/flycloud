package com.fly.mall.api.promotion.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 客服消息 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class KeFuMessageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long conversationId;

    @JsonLongId
    private Long senderId;

    private Integer senderType;

    @JsonLongId
    private Long receiverId;

    private Integer receiverType;

    private Integer contentType;

    private String content;

    private Boolean readStatus;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}
