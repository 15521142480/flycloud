package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.promotion.domain.bo.CouponBo;
import com.fly.mall.api.promotion.domain.vo.CouponVo;
import com.fly.mall.promotion.service.ICouponService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 优惠券 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/coupon")
public class AppCouponController {

    private final ICouponService couponService;

    /**
     * 查询移动端优惠券分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<CouponVo>> list(CouponBo bo, PageBo page) {
        return R.ok(couponService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<CouponVo>> page(CouponBo bo, PageBo page) {
        return R.ok(couponService.queryPageList(bo, page));
    }

    /**
     * 获取移动端优惠券详情。
     */
    @GetMapping("/get/{id}")
    public R<CouponVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(couponService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<CouponVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(couponService.queryById(id));
    }

    /**
     * 领取优惠券。
     */
    @PostMapping("/take")
    public R<Void> take(@RequestBody CouponBo bo) {
        bo.setUserId(UserUtils.getCurUserId());
        return R.ok(couponService.saveOrUpdate(bo));
    }

    /**
     * 获得未使用优惠券数量。
     */
    @GetMapping("/get-unused-count")
    public R<Long> getUnusedCount() {
        CouponBo bo = new CouponBo();
        bo.setUserId(UserUtils.getCurUserId());
        return R.ok(couponService.queryList(bo).stream().filter(item -> item.getUseOrderId() == null).count());
    }

}
