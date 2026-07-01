package com.fly.report.controller.admin.goview;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.fly.common.domain.model.R;
import com.fly.report.service.IGoViewDataService;
import com.fly.system.api.report.domain.bo.GoViewDataGetBySqlBo;
import com.fly.system.api.report.domain.vo.GoViewDataVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

/**
 * 后台 - GoView 数据控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping({"/report/go-view/data", "/admin/report/go-view/data"})
public class GoViewDataController {

    private final IGoViewDataService goViewDataService;

    @RequestMapping("/get-by-sql")
    @PreAuthorize("@pms.hasPermission('report:go-view-data:get-by-sql')")
    public R<GoViewDataVo> getDataBySql(@Valid @RequestBody GoViewDataGetBySqlBo bo) {
        return R.ok(goViewDataService.getDataBySql(bo.getSql()));
    }

    @RequestMapping("/get-by-http")
    @PreAuthorize("@pms.hasPermission('report:go-view-data:get-by-http')")
    public R<GoViewDataVo> getDataByHttp(@RequestParam(required = false) Map<String, String> params,
                                         @RequestBody(required = false) String body) {
        GoViewDataVo respVo = new GoViewDataVo();
        respVo.setDimensions(Arrays.asList("日期", "PV", "UV"));
        respVo.setSource(new LinkedList<>());
        for (int i = 1; i <= 12; i++) {
            String date = "2021-" + (i < 10 ? "0" + i : i);
            Integer pv = RandomUtil.randomInt(1000, 10000);
            Integer uv = RandomUtil.randomInt(100, 1000);
            respVo.getSource().add(MapUtil.<String, Object>builder()
                    .put("日期", date).put("PV", pv).put("UV", uv).build());
        }
        return R.ok(respVo);
    }

}
