package com.fly.system.api.im.domain;

import com.fly.common.domain.BaseEntity;
import com.fly.system.api.im.enums.message.ImMessageStatusEnum;
import com.fly.system.api.im.enums.message.ImMessageTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * IM 私聊消息 DO
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("im_private_message")
@KeySequence("im_private_message_seq")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImPrivateMessage extends BaseEntity {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 客户端消息编号，用于发送幂等
     */
    private String clientMessageId;
    /**
     * 发送人编号
     *
     * 关联 AdminUserDO 的 id 字段
     */
    private Long senderId;
    /**
     * 接收人编号
     *
     * 关联 AdminUserDO 的 id 字段
     */
    private Long receiverId;
    /**
     * 消息类型
     * <p>
     * 枚举 {@link ImMessageTypeEnum}
     */
    private Integer type;
    /**
     * 消息内容，JSON 格式
     *
     * 参考 content 包下的 TextMessage、ImageMessage 等结构化模型
     */
    private String content;
    /**
     * 消息状态
     * <p>
     * 枚举 {@link ImMessageStatusEnum}
     */
    private Integer status;
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

}
