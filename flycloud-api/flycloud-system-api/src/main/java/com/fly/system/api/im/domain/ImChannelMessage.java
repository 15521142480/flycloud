package com.fly.system.api.im.domain;

import com.fly.common.domain.BaseEntity;
import com.fly.system.api.im.typehandler.LongListTypeHandler;
import com.fly.system.api.im.domain.ImChannel;
import com.fly.system.api.im.domain.ImChannelMaterial;
import com.fly.system.api.im.enums.message.ImMessageTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * IM 频道消息 DO
 * <p>
 * 业务语义：
 * - 一次推送 1 行；{@link #receiverUserIds} 为空表示全员
 * - {@link #channelId} 冗余 {@link ImChannelMaterial#getChannelId()} 便于按频道检索
 * - {@link #content} 存推送时 payload 的 JSON 快照（title / coverUrl / summary / url）；不含富文本正文
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "im_channel_message", autoResultMap = true)
@KeySequence("im_channel_message_seq")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImChannelMessage extends BaseEntity {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 频道编号
     * <p>
     * 关联 {@link ImChannel#getId()}；冗余便于按频道检索
     */
    private Long channelId;
    /**
     * 关联素材编号
     * <p>
     * 关联 {@link ImChannelMaterial#getId()}
     */
    private Long materialId;
    /**
     * 消息类型
     * <p>
     * 枚举 {@link ImMessageTypeEnum}
     */
    private Integer type;
    /**
     * 消息内容；推送时 payload 的 JSON 快照
     */
    private String content;
    /**
     * 接收人编号列表；为空表示全员
     */
    @TableField(typeHandler = LongListTypeHandler.class)
    private List<Long> receiverUserIds;
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

}
