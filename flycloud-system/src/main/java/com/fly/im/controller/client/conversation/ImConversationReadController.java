package com.fly.im.controller.client.conversation;

import com.fly.common.domain.model.R;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.im.service.conversation.ImConversationReadService;
import com.fly.system.api.im.domain.ImConversationRead;
import com.fly.system.api.im.domain.vo.ImConversationReadVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "管理后台 - IM 会话读位置")
@RestController
@RequestMapping("/im/conversation-read")
@Validated
public class ImConversationReadController {

    @Resource
    private ImConversationReadService conversationReadService;

    @GetMapping("/pull")
    @Operation(summary = "增量拉取当前用户的会话读位置（重连 / 离线补偿）")
    @Parameters({
            @Parameter(name = "lastUpdateTime", description = "上次拉取到的最新更新时间（毫秒时间戳）；首次拉取不传"),
            @Parameter(name = "lastId", description = "上次拉取到的最后一条记录 id；首次拉取不传"),
            @Parameter(name = "limit", description = "单次拉取条数", required = true)
    })
    public R<List<ImConversationReadVo>> pullMyConversationRead(
            @RequestParam(value = "lastUpdateTime", required = false) Long lastUpdateTime,
            @RequestParam(value = "lastId", required = false) Long lastId,
            @RequestParam("limit") @Min(1) @Max(200) Integer limit) {

        List<ImConversationRead> list = conversationReadService.pullConversationReadList(
                UserUtils.getCurUserId(), lastUpdateTime, lastId, limit);

        return R.ok(BeanUtils.toBean(list, ImConversationReadVo.class));
    }

}
