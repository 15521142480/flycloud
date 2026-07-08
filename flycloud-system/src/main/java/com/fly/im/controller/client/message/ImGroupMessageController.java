package com.fly.im.controller.client.message;

import com.fly.common.domain.model.R;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.message.group.ImGroupMessageListReqVo;
import com.fly.system.api.im.domain.vo.admin.message.group.ImGroupMessageRespVo;
import com.fly.system.api.im.domain.vo.admin.message.group.ImGroupMessageSendReqVo;
import com.fly.system.api.im.domain.message.ImGroupMessage;
import com.fly.im.service.message.ImGroupMessageService;
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

@Tag(name = "管理后台 - IM 群聊消息")
@RestController
@RequestMapping({"/im/message/group", "/admin/im/message/group"})
@Validated
public class ImGroupMessageController {

    @Resource
    private ImGroupMessageService groupMessageService;

    @PostMapping("/send")
    @Operation(summary = "发送群聊消息")
    public R<ImGroupMessageRespVo> sendGroupMessage(@Valid @RequestBody ImGroupMessageSendReqVo reqVo) {
        ImGroupMessage message = groupMessageService.sendGroupMessage(getCurUserId(), reqVo);
        return ok(BeanUtils.toBean(message, ImGroupMessageRespVo.class));
    }

    @GetMapping("/pull")
    @Operation(summary = "拉取群聊消息（增量）")
    @Parameter(name = "minId", description = "最小消息 id", required = true, example = "0")
    @Parameter(name = "size", description = "拉取数量", required = true, example = "100")
    public R<List<ImGroupMessageRespVo>> pullGroupMessageList(
            @RequestParam("minId") Long minId,
            @RequestParam("size") @Min(value = 1, message = "拉取数量最小值为 1") Integer size) {
        List<ImGroupMessage> messages = groupMessageService.pullGroupMessageList(getCurUserId(), minId, size);
        return ok(BeanUtils.toBean(messages, ImGroupMessageRespVo.class));
    }

    @PutMapping("/read")
    @Operation(summary = "标记群聊消息已读")
    @Parameter(name = "groupId", description = "群编号", required = true, example = "1")
    @Parameter(name = "messageId", description = "已读到的消息编号", required = true, example = "100")
    public R<Boolean> readGroupMessages(@RequestParam("groupId") Long groupId,
                                                   @RequestParam("messageId") Long messageId) {
        groupMessageService.readGroupMessages(getCurUserId(), groupId, messageId);
        return R.result(true);
    }

    @DeleteMapping("/recall")
    @Operation(summary = "撤回群聊消息")
    @Parameter(name = "id", description = "消息编号", required = true, example = "1")
    public R<ImGroupMessageRespVo> recallGroupMessage(@RequestParam("id") Long id) {
        ImGroupMessage message = groupMessageService.recallGroupMessage(getCurUserId(), id);
        return ok(BeanUtils.toBean(message, ImGroupMessageRespVo.class));
    }

    @GetMapping("/get-read-user-ids")
    @Operation(summary = "获取群消息已读用户列表")
    @Parameter(name = "groupId", description = "群编号", required = true, example = "1")
    @Parameter(name = "messageId", description = "消息编号", required = true, example = "1")
    public R<List<Long>> getGroupReadUserIds(@RequestParam("groupId") Long groupId,
                                                        @RequestParam("messageId") Long messageId) {
        return ok(groupMessageService.getGroupReadUserIds(getCurUserId(), groupId, messageId));
    }

    @GetMapping("/list")
    @Operation(summary = "查询群聊历史消息")
    public R<List<ImGroupMessageRespVo>> getGroupMessageList(@Valid ImGroupMessageListReqVo reqVo) {
        List<ImGroupMessage> messages = groupMessageService.getGroupMessageList(getCurUserId(), reqVo);
        return ok(BeanUtils.toBean(messages, ImGroupMessageRespVo.class));
    }

}
