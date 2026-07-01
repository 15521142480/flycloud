package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.CouponBo;
import com.fly.mall.api.promotion.domain.vo.CouponVo;
import com.fly.mall.promotion.service.ICouponService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 优惠券 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/coupon")
public class CouponController extends BaseController {

    private final ICouponService couponService;

    /**
     * 查询优惠券分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:coupon:list')")
    @GetMapping("/list")
    public R<PageVo<CouponVo>> list(CouponBo bo, PageBo page) {
        return R.ok(couponService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:coupon:list')")
    @GetMapping("/page")
    public R<PageVo<CouponVo>> page(CouponBo bo, PageBo page) {
        return R.ok(couponService.queryPageList(bo, page));
    }

    /**
     * 查询所有优惠券。
     */
    @GetMapping("/getList")
    public R<List<CouponVo>> allList(CouponBo bo) {
        return R.ok(couponService.queryList(bo));
    }

    /**
     * 获取优惠券详情。
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
     * 新增或修改优惠券。
     */
    @Log(title = "优惠券", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:coupon:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody CouponBo bo) {
        return R.ok(couponService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody CouponBo bo) {
        return R.ok(couponService.saveOrUpdate(bo));
    }

    /**
     * 后台发送优惠券。
     */
    @PostMapping("/send")
    public R<Void> send(@RequestBody CouponBo bo) {
        return R.ok(couponService.saveOrUpdate(bo));
    }

    /**
     * 删除优惠券。
     */
    @Log(title = "优惠券", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:coupon:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(couponService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(couponService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}
