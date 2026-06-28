package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.DiscountProductBo;
import com.fly.mall.api.domain.promotion.vo.DiscountProductVo;
import com.fly.mall.promotion.service.IDiscountProductService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 限时折扣商品 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/discount-product")
public class AppDiscountProductController {

    private final IDiscountProductService discountProductService;

    /**
     * 查询移动端限时折扣商品分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<DiscountProductVo>> list(DiscountProductBo bo, PageBo page) {
        return R.ok(discountProductService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<DiscountProductVo>> page(DiscountProductBo bo, PageBo page) {
        return R.ok(discountProductService.queryPageList(bo, page));
    }

    /**
     * 获取移动端限时折扣商品详情。
     */
    @GetMapping("/get/{id}")
    public R<DiscountProductVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(discountProductService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<DiscountProductVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(discountProductService.queryById(id));
    }

}
