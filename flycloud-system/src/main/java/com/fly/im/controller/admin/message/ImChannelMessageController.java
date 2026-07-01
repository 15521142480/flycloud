package com.fly.im.controller.admin.message;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.message.channel.ImChannelMessagePullRespVo;
import com.fly.system.api.im.domain.message.ImChannelMessage;
import com.fly.system.api.im.enums.message.ImMessageStatusEnum;
import com.fly.im.service.message.ImChannelMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertSet;
import static com.fly.common.security.util.UserUtils.getCurUserId;

@Tag(name = "用户 APP - IM 频道消息")
@RestController
@RequestMapping({"/im/channel/message", "/admin/im/channel/message"})
@Validated
public class ImChannelMessageController {

    @Resource
    private ImChannelMessageService channelMessageService;

    @GetMapping("/pull")
    @Operation(summary = "拉取频道消息（离线增量）；按 minId 游标分页")
    public R<List<ImChannelMessagePullRespVo>> pull(
            @RequestParam(value = "minId", defaultValue = "0") @PositiveOrZero(message = "minId 不能小于 0") Long minId,
            @RequestParam(value = "size", defaultValue = "100")
            @Min(value = 1, message = "size 必须大于 0")
            @Max(value = 200, message = "size 一次最多 200 条") Integer size) {
        // 1. 拉取消息列表
        Long userId = getCurUserId();
        List<ImChannelMessage> list = channelMessageService.getMessageListForPull(userId, minId, size);
        if (CollUtil.isEmpty(list)) {
            return ok(Collections.emptyList());
        }
        // 2. 按 Redis 已读游标补 status；device A 已读后 device B 拉到这条不再算入未读
        Map<Long, Long> readMaxByChannel = channelMessageService.getChannelReadMaxMessageIdMap(
                userId, convertSet(list, ImChannelMessage::getChannelId));
        return ok(BeanUtils.toBean(list, ImChannelMessagePullRespVo.class, vo -> {
            Long readMax = readMaxByChannel.get(vo.getChannelId());
            vo.setStatus(readMax != null && readMax >= vo.getId()
                    ? ImMessageStatusEnum.READ.getStatus()
                    : ImMessageStatusEnum.UNREAD.getStatus());
        }));
    }

    @PutMapping("/read")
    @Operation(summary = "标记频道消息已读")
    @Parameter(name = "channelId", description = "频道编号", required = true, example = "1")
    @Parameter(name = "messageId", description = "已读到的消息编号", required = true, example = "100")
    public R<Boolean> readChannelMessages(@RequestParam("channelId") Long channelId,
                                                     @RequestParam("messageId") Long messageId) {
        channelMessageService.readChannelMessages(getCurUserId(), channelId, messageId);
        return ok(true);
    }

}
