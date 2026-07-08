package com.fly.im.controller.admin.channel;

import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.manager.channel.channel.ImChannelPageReqVo;
import com.fly.system.api.im.domain.vo.admin.manager.channel.channel.ImChannelRespVo;
import com.fly.system.api.im.domain.vo.admin.manager.channel.channel.ImChannelSaveReqVo;
import com.fly.system.api.im.domain.channel.ImChannel;
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
@RequestMapping({"/im/manager/channel", "/admin/im/manager/channel"})
@Validated
public class ImChannelManagerController {

    @Resource
    private ImChannelService channelService;



    @PostMapping("/create")
    @Operation(summary = "新增频道")
    @PreAuthorize("@pms.hasPermission('im:channel:list:saveOrUpdate')")
    public R<Long> createChannel(@Valid @RequestBody ImChannelSaveReqVo reqVo) {
        return ok(channelService.createChannel(reqVo));
    }

    @PutMapping("/update")
    @Operation(summary = "修改频道")
    @PreAuthorize("@pms.hasPermission('im:channel:list:saveOrUpdate')")
    public R<Boolean> updateChannel(@Valid @RequestBody ImChannelSaveReqVo reqVo) {
        channelService.updateChannel(reqVo);
        return R.result(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除频道")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:channel:list:delete')")
    public R<Boolean> deleteChannel(@RequestParam("id") Long id) {
        channelService.deleteChannel(id);
        return R.result(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得频道分页")
    @PreAuthorize("@pms.hasPermission('im:channel:list:list')")
    public R<PageResult<ImChannelRespVo>> getChannelPage(@Valid ImChannelPageReqVo pageReqVo) {
        PageResult<ImChannel> pageResult = channelService.getChannelPage(pageReqVo);
        return ok(PageResult.convert(pageResult, ImChannelRespVo.class,
                vo -> vo.setAvatar(vo.getAvatar())));
    }

    @GetMapping("/get")
    @Operation(summary = "获得频道详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:channel:list:list')")
    public R<ImChannelRespVo> getChannel(@RequestParam("id") Long id) {
        ImChannel channel = channelService.getChannel(id);
        ImChannelRespVo vo = BeanUtils.toBean(channel, ImChannelRespVo.class);
        vo.setAvatar(vo.getAvatar());
        return ok(vo);
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得启用的频道精简列表；前端表单选择频道时调用")
    public R<List<ImChannelRespVo>> getSimpleChannelList() {
        // getChannelListByStatus 统一命名
        List<ImChannel> list = channelService.getChannelListByStatus(CommonStatusEnum.ENABLE.getStatus());
        List<ImChannelRespVo> respList = BeanUtils.toBean(list, ImChannelRespVo.class);
        respList.forEach(vo -> vo.setAvatar(vo.getAvatar()));
        return ok(respList);
    }

}
