package com.fly.mall.api.promotion.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客服消息 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "promotion_kefu_message")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "客服消息对象", description = "客服消息表")
public class KeFuMessage extends BaseEntity {

    @TableId
    private Long id;

    private Long conversationId;

    private Long senderId;

    private Integer senderType;

    private Long receiverId;

    private Integer receiverType;

    private Integer contentType;

    private String content;

    private Boolean readStatus;

}
