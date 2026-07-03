package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.model.R;
import com.fly.mall.api.promotion.domain.vo.AppSeckillConfigRespVo;
import com.fly.mall.promotion.service.ISeckillConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 秒杀时段配置 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/seckill-config")
public class AppSeckillConfigController {

    private final ISeckillConfigService seckillConfigService;

    /**
     * 获得秒杀时间段列表。
     */
    @GetMapping("/list")
    public R<List<AppSeckillConfigRespVo>> list() {
        return R.ok(seckillConfigService.queryAppList());
    }

}
