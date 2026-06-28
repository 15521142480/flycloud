package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.BargainHelpBo;
import com.fly.mall.api.domain.promotion.vo.BargainHelpVo;
import com.fly.mall.promotion.service.IBargainHelpService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 砍价助力 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/bargain-help")
public class AppBargainHelpController {

    private final IBargainHelpService bargainHelpService;

    /**
     * 查询移动端砍价助力分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<BargainHelpVo>> list(BargainHelpBo bo, PageBo page) {
        return R.ok(bargainHelpService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<BargainHelpVo>> page(BargainHelpBo bo, PageBo page) {
        return R.ok(bargainHelpService.queryPageList(bo, page));
    }

    /**
     * 获取移动端砍价助力详情。
     */
    @GetMapping("/get/{id}")
    public R<BargainHelpVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(bargainHelpService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<BargainHelpVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(bargainHelpService.queryById(id));
    }

}
