package com.fly.im.controller.admin.manager.channel;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.util.MapUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.im.controller.admin.manager.channel.vo.material.ImChannelMaterialPageReqVo;
import com.fly.im.controller.admin.manager.channel.vo.material.ImChannelMaterialRespVo;
import com.fly.im.controller.admin.manager.channel.vo.material.ImChannelMaterialSaveReqVo;
import com.fly.im.dal.dataobject.channel.ImChannelDO;
import com.fly.im.dal.dataobject.channel.ImChannelMaterialDO;
import com.fly.im.service.channel.ImChannelMaterialService;
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
import java.util.Map;

import static com.fly.common.domain.model.R.ok;
import static com.fly.common.utils.collection.CollectionUtils.convertMap;
import static com.fly.common.utils.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - IM 频道素材")
@RestController
@RequestMapping("/im/manager/channel-material")
@Validated
public class ImChannelMaterialManagerController {

    @Resource
    private ImChannelMaterialService channelMaterialService;
    @Resource
    private ImChannelService channelService;

    @PostMapping("/create")
    @Operation(summary = "新增素材")
    @PreAuthorize("@pms.hasPermission('im:manager:channel-material:create')")
    public R<Long> createMaterial(@Valid @RequestBody ImChannelMaterialSaveReqVo reqVo) {
        return ok(channelMaterialService.createMaterial(reqVo));
    }

    @PutMapping("/update")
    @Operation(summary = "修改素材")
    @PreAuthorize("@pms.hasPermission('im:manager:channel-material:update')")
    public R<Boolean> updateMaterial(@Valid @RequestBody ImChannelMaterialSaveReqVo reqVo) {
        channelMaterialService.updateMaterial(reqVo);
        return ok(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除素材")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:channel-material:delete')")
    public R<Boolean> deleteMaterial(@RequestParam("id") Long id) {
        channelMaterialService.deleteMaterial(id);
        return ok(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得素材分页；含频道名回填")
    @PreAuthorize("@pms.hasPermission('im:manager:channel-material:query')")
    public R<PageResult<ImChannelMaterialRespVo>> getMaterialPage(@Valid ImChannelMaterialPageReqVo pageReqVo) {
        PageResult<ImChannelMaterialDO> pageResult = channelMaterialService.getMaterialPage(pageReqVo);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return ok(PageResult.empty(pageResult.getTotal()));
        }
        // 回填频道名
        List<ImChannelDO> channels = channelService.getChannelList(
                convertSet(pageResult.getList(), ImChannelMaterialDO::getChannelId));
        Map<Long, ImChannelDO> channelMap = convertMap(channels, ImChannelDO::getId);
        return ok(PageResult.convert(pageResult, ImChannelMaterialRespVo.class, vo ->
                MapUtils.findAndThen(channelMap, vo.getChannelId(), c -> vo.setChannelName(c.getName()))));
    }

    @GetMapping("/get")
    @Operation(summary = "获得素材详情（含富文本正文）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('im:manager:channel-material:query')")
    public R<ImChannelMaterialRespVo> getMaterial(@RequestParam("id") Long id) {
        ImChannelMaterialDO material = channelMaterialService.getMaterial(id);
        return ok(BeanUtils.toBean(material, ImChannelMaterialRespVo.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得指定频道下的素材精简列表；用于推送弹窗的素材下拉")
    @Parameter(name = "channelId", description = "频道编号", required = true, example = "1")
    public R<List<ImChannelMaterialRespVo>> getSimpleMaterialList(@RequestParam("channelId") Long channelId) {
        List<ImChannelMaterialDO> list = channelMaterialService.getMaterialListByChannelId(channelId);
        return ok(BeanUtils.toBean(list, ImChannelMaterialRespVo.class));
    }

}
