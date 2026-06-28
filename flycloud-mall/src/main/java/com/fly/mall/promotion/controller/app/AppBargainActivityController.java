package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.BargainActivityBo;
import com.fly.mall.api.domain.promotion.vo.BargainActivityVo;
import com.fly.mall.promotion.service.IBargainActivityService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 砍价活动 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/bargain-activity")
public class AppBargainActivityController {

    private final IBargainActivityService bargainActivityService;

    /**
     * 查询移动端砍价活动分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<BargainActivityVo>> list(BargainActivityBo bo, PageBo page) {
        return R.ok(bargainActivityService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<BargainActivityVo>> page(BargainActivityBo bo, PageBo page) {
        return R.ok(bargainActivityService.queryPageList(bo, page));
    }

    /**
     * 获取移动端砍价活动详情。
     */
    @GetMapping("/get/{id}")
    public R<BargainActivityVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(bargainActivityService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<BargainActivityVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(bargainActivityService.queryById(id));
    }

}
