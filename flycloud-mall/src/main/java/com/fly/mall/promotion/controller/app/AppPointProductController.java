package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.PointProductBo;
import com.fly.mall.api.domain.promotion.vo.PointProductVo;
import com.fly.mall.promotion.service.IPointProductService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 积分商城商品 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/point-product")
public class AppPointProductController {

    private final IPointProductService pointProductService;

    /**
     * 查询移动端积分商城商品分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<PointProductVo>> list(PointProductBo bo, PageBo page) {
        return R.ok(pointProductService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<PointProductVo>> page(PointProductBo bo, PageBo page) {
        return R.ok(pointProductService.queryPageList(bo, page));
    }

    /**
     * 获取移动端积分商城商品详情。
     */
    @GetMapping("/get/{id}")
    public R<PointProductVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(pointProductService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<PointProductVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(pointProductService.queryById(id));
    }

}
