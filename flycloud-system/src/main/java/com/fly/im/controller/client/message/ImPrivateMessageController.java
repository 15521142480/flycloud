package com.fly.im.controller.client.message;

import com.fly.common.domain.model.R;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.message.privates.ImPrivateMessageListReqVo;
import com.fly.system.api.im.domain.vo.admin.message.privates.ImPrivateMessageRespVo;
import com.fly.system.api.im.domain.vo.admin.message.privates.ImPrivateMessageSendReqVo;
import com.fly.system.api.im.domain.message.ImPrivateMessage;
import com.fly.im.service.message.ImPrivateMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.security.util.UserUtils.getCurUserId;

@Tag(name = "管理后台 - IM 私聊消息")
@RestController
@RequestMapping({"/im/message/private", "/admin/im/message/private"})
@Validated
public class ImPrivateMessageController {

    @Resource
    private ImPrivateMessageService privateMessageService;

    @PostMapping("/send")
    @Operation(summary = "发送私聊消息")
    public R<ImPrivateMessageRespVo> sendPrivateMessage(
            @Valid @RequestBody ImPrivateMessageSendReqVo reqVo) {
        ImPrivateMessage message = privateMessageService.sendPrivateMessage(getCurUserId(), reqVo);
        return ok(BeanUtils.toBean(message, ImPrivateMessageRespVo.class));
    }

    @GetMapping("/pull")
    @Operation(summary = "拉取私聊消息（增量）")
    @Parameter(name = "minId", description = "最小消息 id", required = true, example = "0")
    @Parameter(name = "size", description = "拉取数量", required = true, example = "100")
    public R<List<ImPrivateMessageRespVo>> pullPrivateMessageList(
            @RequestParam("minId") Long minId,
            @RequestParam("size") @Min(value = 1, message = "拉取数量最小值为 1") Integer size) {
        List<ImPrivateMessage> messages = privateMessageService.pullPrivateMessageList(getCurUserId(), minId, size);
        return ok(BeanUtils.toBean(messages, ImPrivateMessageRespVo.class));
    }

    @PutMapping("/read")
    @Operation(summary = "标记私聊消息已读")
    @Parameter(name = "receiverId", description = "接收方用户编号（对方）", required = true, example = "2")
    @Parameter(name = "messageId", description = "已读位置（含），通常是会话内最大消息编号", required = true, example = "100")
    public R<Boolean> readPrivateMessages(@RequestParam("receiverId") Long receiverId,
                                                     @RequestParam("messageId") Long messageId) {
        privateMessageService.readPrivateMessages(getCurUserId(), receiverId, messageId);
        return R.result(true);
    }

    @GetMapping("/max-read-message-id")
    @Operation(summary = "查询对方已读到我发的最大消息 id",
            description = "用于多端 / 离线场景下的已读位置补齐：进入会话或断线重连后调用，结果用于翻转本地自发消息状态")
    @Parameter(name = "peerId", description = "对方用户编号", required = true, example = "2")
    public R<Long> getMaxReadMessageId(@RequestParam("peerId") Long peerId) {
        return ok(privateMessageService.getMaxReadMessageId(getCurUserId(), peerId));
    }

    @DeleteMapping("/recall")
    @Operation(summary = "撤回私聊消息")
    @Parameter(name = "id", description = "消息编号", required = true, example = "1")
    public R<ImPrivateMessageRespVo> recallPrivateMessage(@RequestParam("id") Long id) {
        ImPrivateMessage message = privateMessageService.recallPrivateMessage(getCurUserId(), id);
        return ok(BeanUtils.toBean(message, ImPrivateMessageRespVo.class));
    }

    @GetMapping("/list")
    @Operation(summary = "查询私聊历史消息")
    public R<List<ImPrivateMessageRespVo>> getPrivateMessageList(@Valid ImPrivateMessageListReqVo reqVo) {
        List<ImPrivateMessage> messages = privateMessageService.getPrivateMessageList(getCurUserId(), reqVo);
        return ok(BeanUtils.toBean(messages, ImPrivateMessageRespVo.class));
    }

}
