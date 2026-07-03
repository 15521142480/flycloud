package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.CouponTemplateBo;
import com.fly.mall.api.promotion.domain.vo.CouponTemplateVo;
import com.fly.mall.api.product.domain.vo.ProductSpuVo;
import com.fly.mall.product.service.IProductSpuService;
import com.fly.mall.promotion.service.ICouponService;
import com.fly.mall.promotion.service.ICouponTemplateService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 优惠券模板 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/coupon-template")
public class AppCouponTemplateController {

    private final ICouponTemplateService couponTemplateService;
    private final ICouponService couponService;
    private final IProductSpuService productSpuService;

    /**
     * 查询移动端优惠券模板分页列表。
     */
    @GetMapping("/list")
    public R<List<CouponTemplateVo>> list(@RequestParam(value = "spuId", required = false) Long spuId,
                                          @RequestParam(value = "productScope", required = false) Integer productScope,
                                          @RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        Long productScopeValue = getProductScopeValue(productScope, spuId);
        List<CouponTemplateVo> list = couponTemplateService.queryCanTakeList(productScope, productScopeValue, count);
        fillCanTake(list);
        return R.ok(list);
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<CouponTemplateVo>> page(CouponTemplateBo bo, PageBo page) {
        PageVo<CouponTemplateVo> pageVo = couponTemplateService.queryPageList(bo, page);
        fillCanTake(pageVo.getList());
        return R.ok(pageVo);
    }

    /**
     * 获取移动端优惠券模板详情。
     */
    @GetMapping("/get/{id}")
    public R<CouponTemplateVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        CouponTemplateVo template = couponTemplateService.queryById(id);
        if (template == null) {
            return R.ok((CouponTemplateVo) null);
        }
        fillCanTake(List.of(template));
        return R.ok(template);
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<CouponTemplateVo> getDetail(@RequestParam("id") Long id) {
        return getInfo(id);
    }

    /**
     * 根据编号列表查询优惠券模板。
     */
    @GetMapping("/list-by-ids")
    public R<List<CouponTemplateVo>> listByIds(@RequestParam("ids") List<Long> ids) {
        List<CouponTemplateVo> list = couponTemplateService.queryList(ids);
        fillCanTake(list);
        return R.ok(list);
    }

    /**
     * 计算优惠券模板范围值。
     */
    private Long getProductScopeValue(Integer productScope, Long spuId) {
        if (spuId == null || productScope == null || productScope == 1) {
            return null;
        }
        if (productScope == 3) {
            ProductSpuVo spu = productSpuService.queryById(spuId);
            return spu == null ? null : spu.getCategoryId();
        }
        return spuId;
    }

    /**
     * 补充当前用户是否可领取。
     */
    private void fillCanTake(List<CouponTemplateVo> templates) {
        if (templates == null) {
            return;
        }
        Long userId = com.fly.common.security.util.UserUtils.getCurUserId();
        for (CouponTemplateVo template : templates) {
            if (template != null) {
                template.setCanTake(couponService.canTake(template.getId(), userId));
            }
        }
    }

}
