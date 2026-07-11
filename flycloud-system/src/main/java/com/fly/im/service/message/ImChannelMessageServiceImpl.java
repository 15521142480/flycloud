package com.fly.im.service.message;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fly.common.security.util.UserUtils;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.utils.json.JsonUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.bo.ImChannelMessagePageBo;
import com.fly.system.api.im.domain.bo.ImChannelMessageBo;
import com.fly.system.api.im.domain.ImChannelMaterial;
import com.fly.system.api.im.domain.ImChannelMessage;
import com.fly.im.mapper.ImChannelMessageMapper;
import com.fly.im.service.conversation.ImConversationReadService;
import com.fly.system.api.im.enums.ImConversationTypeEnum;
import com.fly.system.api.im.enums.message.ImMessageTypeEnum;
import com.fly.im.service.channel.ImChannelMaterialService;
import com.fly.im.service.websocket.ImWebSocketService;
import com.fly.im.service.websocket.dto.ImChannelMessageDTO;
import com.fly.im.service.websocket.dto.message.MaterialMessage;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.fly.im.framework.exception.ServiceExceptionUtil.exception;
import static com.fly.system.api.im.enums.ErrorCodeConstants.IM_CHANNEL_MESSAGE_NOT_EXISTS;

/**
 * IM 频道消息 Service 实现类
 *
 * @author lxs
 * @date 2026-07-02
 */
@Service
@Validated
public class ImChannelMessageServiceImpl implements ImChannelMessageService {

    @Resource
    private ImChannelMessageMapper channelMessageMapper;

    @Resource
    private ImChannelMaterialService channelMaterialService;
    @Resource
    private ImWebSocketService webSocketService;

    @Resource
    private ImConversationReadService conversationReadService;

    // ==================== 用户端 ====================

    @Override
    public List<ImChannelMessage> getMessageListForPull(Long userId, Long minId, Integer size) {
        return channelMessageMapper.selectListByUserAndMinId(userId, minId, size);
    }

    @Override
    public Map<Long, Long> getChannelReadMaxMessageIdMap(Long userId, Collection<Long> channelIds) {
        return conversationReadService.getConversationReadMessageIdMap(
                userId, ImConversationTypeEnum.CHANNEL.getType(), channelIds);
    }

    @Override
    public void readChannelMessages(Long userId, Long channelId, Long messageId) {
        Assert.notNull(channelId, "频道编号不能为空");
        Assert.notNull(messageId, "已读消息编号不能为空");
        // 1. 仅允许推进当前频道内、当前用户可见的真实消息，防止伪造 messageId 污染读游标。
        ImChannelMessage message = channelMessageMapper.selectById(messageId);
        if (message == null || ObjUtil.notEqual(message.getChannelId(), channelId)) {
            return;
        }
        if (CollUtil.isNotEmpty(message.getReceiverUserIds()) && !message.getReceiverUserIds().contains(userId)) {
            return;
        }

        // 2. 数据库游标单调前进；未前进时无需重复广播多端 READ 事件。
        boolean advanced = conversationReadService.updateConversationReadPosition(
                userId, ImConversationTypeEnum.CHANNEL.getType(), channelId, messageId);
        if (!advanced) {
            return;
        }

        // 3. 异步推 READ 事件给自己多端同步
        getSelf().readChannelMessageEvent(userId, channelId, messageId);
    }

    /**
     * 发送频道已读 READ 事件给自己其他终端；频道无「给发送方刷回执」概念，不广播
     */
    @Async
    public void readChannelMessageEvent(Long userId, Long channelId, Long readId) {
        webSocketService.sendChannelMessageAsync(userId, ImChannelMessageDTO.ofRead(channelId, readId));
    }

    private ImChannelMessageServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }

    // ==================== 管理后台 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendMessage(ImChannelMessageBo reqVo) {
        // 1. 校验素材存在
        ImChannelMaterial material = channelMaterialService.validateMaterialExists(reqVo.getMaterialId());

        // 2.1 组装 payload（不带富文本正文）；字段同名直接 BeanUtils 拷贝，materialId 单独 set 以兼容转发场景
        MaterialMessage payload = BeanUtils.toBean(material, MaterialMessage.class).setMaterialId(material.getId());
        String payloadJson = JsonUtils.toJsonString(payload);
        // 2.2 落库 1 行 message；reqVo 同名字段（materialId / receiverUserIds）自动拷贝，剩余字段补 set
        ImChannelMessage message = BeanUtils.toBean(reqVo, ImChannelMessage.class).setChannelId(material.getChannelId())
                .setType(ImMessageTypeEnum.MATERIAL.getType()).setContent(payloadJson).setSendTime(LocalDateTime.now());
        material.setCreateBy(UserUtils.getCurUserIdStr());
        material.setCreateTime(LocalDateTime.now());
        channelMessageMapper.insert(message);

        // 3. 异步推 WebSocket：指定用户走点对点；全员（receiverUserIds 为空）走广播
        ImChannelMessageDTO dto = ImChannelMessageDTO.ofSend(message);
        if (CollUtil.isNotEmpty(reqVo.getReceiverUserIds())) {
            webSocketService.sendChannelMessageAsync(reqVo.getReceiverUserIds(), dto);
        } else {
            webSocketService.broadcastChannelMessageAsync(dto);
        }
        return message.getId();
    }

    @Override
    public PageResult<ImChannelMessage> getMessagePage(ImChannelMessagePageBo reqVo) {
        return channelMessageMapper.selectPage(reqVo);
    }

    @Override
    public void deleteMessage(Long id) {
        if (channelMessageMapper.selectById(id) == null) {
            throw exception(IM_CHANNEL_MESSAGE_NOT_EXISTS);
        }
        channelMessageMapper.deleteById(id);
    }

}
