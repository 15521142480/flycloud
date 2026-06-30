package com.fly.im.controller.admin.manager.channel;

import com.fly.im.framework.enums.CommonStatusEnum;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.manager.channel.vo.channel.ImChannelPageReqVo;
import com.fly.im.controller.admin.manager.channel.vo.channel.ImChannelRespVo;
import com.fly.im.controller.admin.manager.channel.vo.channel.ImChannelSaveReqVo;
import com.fly.im.dal.dataobject.channel.ImChannelDO;
import com.fly.im.service.channel.ImChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fly.common.domain.model.R.ok;

@Tag(name = "管理后台 - IM 频道")
@RestController
@RequestMapping("/im/manager/channel")
@Validated
public class ImChannelManagerController {

    @Resource
    private ImChannelService channelService;

    @PostMapping("/create")
    @Operation(summary = "新增频道")
    @PreAuthorize("@pms.hasPermission('im:manager:channel:create')")
    public R<Long> createChannel(@Valid @RequestBody ImChannelSaveReqVo reqVo) {
        return ok(channelService.createChannel(reqVo));
    }

    @PutMapping("/update")
    @Operation(summary = "修改频道")
    @PreAuthorize("@pms.hasPermission('im:manager:channel:update')")
    public R<Boolean> updateChannel(@Valid @RequestBody ImChannelSaveReqVo reqVo) {
        channelService.updateChannel(reqVo);
        return ok(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除频道")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:channel:delete')")
    public R<Boolean> deleteChannel(@RequestParam("id") Long id) {
        channelService.deleteChannel(id);
        return ok(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得频道分页")
    @PreAuthorize("@pms.hasPermission('im:manager:channel:query')")
    public R<PageResult<ImChannelRespVo>> getChannelPage(@Valid ImChannelPageReqVo pageReqVo) {
        PageResult<ImChannelDO> pageResult = channelService.getChannelPage(pageReqVo);
        return ok(PageResult.convert(pageResult, ImChannelRespVo.class));
    }

    @GetMapping("/get")
    @Operation(summary = "获得频道详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:channel:query')")
    public R<ImChannelRespVo> getChannel(@RequestParam("id") Long id) {
        ImChannelDO channel = channelService.getChannel(id);
        return ok(BeanUtils.toBean(channel, ImChannelRespVo.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得启用的频道精简列表；前端表单选择频道时调用")
    public R<List<ImChannelRespVo>> getSimpleChannelList() {
        // getChannelListByStatus 统一命名
        List<ImChannelDO> list = channelService.getChannelListByStatus(CommonStatusEnum.ENABLE.getStatus());
        return ok(BeanUtils.toBean(list, ImChannelRespVo.class));
    }

}
