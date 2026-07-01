package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.RewardActivityBo;
import com.fly.mall.api.promotion.domain.vo.RewardActivityVo;
import com.fly.mall.promotion.service.IRewardActivityService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 满减送活动 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/reward-activity")
public class AppRewardActivityController {

    private final IRewardActivityService rewardActivityService;

    /**
     * 查询移动端满减送活动分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<RewardActivityVo>> list(RewardActivityBo bo, PageBo page) {
        return R.ok(rewardActivityService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<RewardActivityVo>> page(RewardActivityBo bo, PageBo page) {
        return R.ok(rewardActivityService.queryPageList(bo, page));
    }

    /**
     * 获取移动端满减送活动详情。
     */
    @GetMapping("/get/{id}")
    public R<RewardActivityVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(rewardActivityService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<RewardActivityVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(rewardActivityService.queryById(id));
    }

}
