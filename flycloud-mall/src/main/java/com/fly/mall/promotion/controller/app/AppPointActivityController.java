package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.vo.AppPointActivityDetailRespVo;
import com.fly.mall.api.promotion.domain.vo.AppPointActivityRespVo;
import com.fly.mall.promotion.service.IPointActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 积分商城活动 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/point-activity")
public class AppPointActivityController {

    private final IPointActivityService pointActivityService;

    /**
     * 获得积分商城活动分页。
     */
    @GetMapping("/page")
    public R<PageVo<AppPointActivityRespVo>> page(PageBo page) {
        return R.ok(pointActivityService.queryAppPageList(page));
    }

    /**
     * 获得积分商城活动明细。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<AppPointActivityDetailRespVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(pointActivityService.queryAppDetail(id));
    }

    /**
     * 根据编号列表查询积分商城活动。
     */
    @GetMapping("/list-by-ids")
    public R<List<AppPointActivityRespVo>> listByIds(@RequestParam("ids") List<Long> ids) {
        return R.ok(pointActivityService.queryAppListByIds(ids));
    }

}
