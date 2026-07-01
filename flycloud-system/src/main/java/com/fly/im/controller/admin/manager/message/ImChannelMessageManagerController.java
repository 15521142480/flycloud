package com.fly.im.controller.admin.manager.message;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.manager.message.channel.ImChannelMessagePageReqVo;
import com.fly.system.api.im.domain.vo.admin.manager.message.channel.ImChannelMessageRespVo;
import com.fly.system.api.im.domain.vo.admin.manager.message.channel.ImChannelMessageSendReqVo;
import com.fly.system.api.im.domain.channel.ImChannel;
import com.fly.system.api.im.domain.channel.ImChannelMaterial;
import com.fly.system.api.im.domain.message.ImChannelMessage;
import com.fly.im.service.channel.ImChannelMaterialService;
import com.fly.im.service.message.ImChannelMessageService;
import com.fly.im.service.channel.ImChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - IM 频道消息")
@RestController
@RequestMapping({"/im/manager/channel-message", "/admin/im/manager/channel-message"})
@Validated
public class ImChannelMessageManagerController {

    @Resource
    private ImChannelMessageService channelMessageService;
    @Resource
    private ImChannelService channelService;
    @Resource
    private ImChannelMaterialService channelMaterialService;

    @PostMapping("/send")
    @Operation(summary = "立即推送频道消息")
    @PreAuthorize("@pms.hasPermission('im:manager:channel-message:send')")
    public R<Long> sendMessage(@Valid @RequestBody ImChannelMessageSendReqVo reqVo) {
        return ok(channelMessageService.sendMessage(reqVo));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除频道消息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:channel-message:delete')")
    public R<Boolean> deleteMessage(@RequestParam("id") Long id) {
        channelMessageService.deleteMessage(id);
        return ok(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得频道消息分页；回填频道名 / 素材标题")
    @PreAuthorize("@pms.hasPermission('im:manager:channel-message:query')")
    public R<PageResult<ImChannelMessageRespVo>> getMessagePage(@Valid ImChannelMessagePageReqVo pageReqVo) {
        PageResult<ImChannelMessage> pageResult = channelMessageService.getMessagePage(pageReqVo);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return ok(PageResult.empty(pageResult.getTotal()));
        }
        // 批量查询频道和素材，并回填频道名 / 素材标题
        Map<Long, ImChannel> channelMap = channelService.getChannelMap(
                convertSet(pageResult.getList(), ImChannelMessage::getChannelId));
        Map<Long, ImChannelMaterial> materialMap = channelMaterialService.getMaterialMap(
                convertSet(pageResult.getList(), ImChannelMessage::getMaterialId));
        return ok(PageResult.convert(pageResult, ImChannelMessageRespVo.class, vo -> {
            MapUtils.findAndThen(channelMap, vo.getChannelId(), c -> vo.setChannelName(c.getName()));
            MapUtils.findAndThen(materialMap, vo.getMaterialId(),
                    material -> vo.setMaterialTitle(material.getTitle()).setMaterialCoverUrl(material.getCoverUrl()));
        }));
    }

}
