package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.vo.AppBargainActivityDetailRespVo;
import com.fly.mall.api.promotion.domain.vo.AppBargainActivityRespVo;
import com.fly.mall.promotion.service.IBargainActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 砍价活动 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/bargain-activity")
public class AppBargainActivityController {

    private final IBargainActivityService bargainActivityService;

    /**
     * 获得砍价活动列表。
     */
    @GetMapping("/list")
    public R<List<AppBargainActivityRespVo>> list(@RequestParam(name = "count", defaultValue = "6") Integer count) {
        return R.ok(bargainActivityService.queryAppList(count));
    }

    /**
     * 获得砍价活动分页。
     */
    @GetMapping("/page")
    public R<PageVo<AppBargainActivityRespVo>> page(PageBo page) {
        return R.ok(bargainActivityService.queryAppPageList(page));
    }

    /**
     * 获得砍价活动详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<AppBargainActivityDetailRespVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(bargainActivityService.queryAppDetail(id));
    }

}
