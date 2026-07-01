package com.fly.ip.controller;

import cn.hutool.core.lang.Assert;
import com.fly.common.domain.Area;
import com.fly.common.domain.model.R;
import com.fly.common.utils.BeanUtils;
import com.fly.common.utils.ip.AreaUtils;
import com.fly.common.utils.ip.IPUtils;
import com.fly.system.api.ip.domain.vo.AreaNodeRespVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "管理后台 - 地区")
@RestController
@RequestMapping("/area")
@Validated
public class AreaController {


    @GetMapping("/tree")
    @Operation(summary = "获得地区树")
    public R<List<AreaNodeRespVo>> getAreaTree() {

        Area area = AreaUtils.getArea(Area.ID_CHINA);
        Assert.notNull(area, "获取不到中国");
        return R.ok(BeanUtils.toBean(area.getChildren(), AreaNodeRespVo.class));
    }

    @GetMapping("/get-by-ip")
    @Operation(summary = "获得 IP 对应的地区名")
    @Parameter(name = "ip", description = "IP", required = true)
    public R<String> getAreaByIp(@RequestParam("ip") String ip) {

        // 获得城市
        Area area = IPUtils.getArea(ip);
        if (area == null) {
            return R.failed("未知ip");
        }
        return R.ok(AreaUtils.format(area.getId()));
    }

}
