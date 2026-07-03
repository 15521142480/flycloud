package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillActivityDetailRespVo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillActivityNowRespVo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillActivityRespVo;
import com.fly.mall.promotion.service.ISeckillActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 秒杀活动 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/seckill-activity")
public class AppSeckillActivityController {

    private final ISeckillActivityService seckillActivityService;

    /**
     * 获得秒杀活动分页。
     */
    @GetMapping("/page")
    public R<PageVo<AppSeckillActivityRespVo>> page(@RequestParam(value = "configId", required = false) Long configId,
                                                    PageBo page) {
        return R.ok(seckillActivityService.queryAppPageList(configId, page));
    }

    /**
     * 获得秒杀活动明细。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<AppSeckillActivityDetailRespVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(seckillActivityService.queryAppDetail(id));
    }

    /**
     * 根据编号列表查询秒杀活动。
     */
    @GetMapping("/list-by-ids")
    public R<List<AppSeckillActivityRespVo>> listByIds(@RequestParam("ids") List<Long> ids) {
        return R.ok(seckillActivityService.queryAppListByIds(ids));
    }

    /**
     * 获得当前秒杀活动。
     */
    @GetMapping("/get-now")
    public R<AppSeckillActivityNowRespVo> getNow() {
        return R.ok(seckillActivityService.queryAppNow());
    }

}
