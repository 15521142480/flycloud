package com.fly.im.controller.admin.channel;

import com.fly.common.domain.model.R;
import com.fly.common.utils.BeanUtils;
import com.fly.system.api.im.domain.vo.admin.manager.channel.material.ImChannelMaterialRespVo;
import com.fly.system.api.im.domain.channel.ImChannelMaterial;
import com.fly.im.service.channel.ImChannelMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.fly.common.domain.model.R.ok;

@Tag(name = "用户 APP - IM 频道素材")
@RestController
@RequestMapping({"/im/channel/material", "/admin/im/channel/material"})
@Validated
public class ImChannelMaterialController {

    @Resource
    private ImChannelMaterialService channelMaterialService;

    @GetMapping("/get")
    @Operation(summary = "获取素材详情；用于客户端点击图文卡片渲染详情页")
    @Parameter(name = "id", description = "素材编号", required = true, example = "1024")
    public R<ImChannelMaterialRespVo> getMaterial(@RequestParam("id") Long id) {
        ImChannelMaterial material = channelMaterialService.validateMaterialExists(id);
        return ok(BeanUtils.toBean(material, ImChannelMaterialRespVo.class));
    }

}
