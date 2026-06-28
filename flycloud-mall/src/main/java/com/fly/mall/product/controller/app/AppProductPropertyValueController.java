package com.fly.mall.product.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.product.bo.ProductPropertyValueBo;
import com.fly.mall.api.domain.product.vo.ProductPropertyValueVo;
import com.fly.mall.product.service.IProductPropertyValueService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 商品属性值 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/product/product-property-value")
public class AppProductPropertyValueController {

    private final IProductPropertyValueService productPropertyValueService;

    /**
     * 查询移动端商品属性值分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<ProductPropertyValueVo>> list(ProductPropertyValueBo bo, PageBo page) {
        return R.ok(productPropertyValueService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<ProductPropertyValueVo>> page(ProductPropertyValueBo bo, PageBo page) {
        return R.ok(productPropertyValueService.queryPageList(bo, page));
    }

    /**
     * 获取移动端商品属性值详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductPropertyValueVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productPropertyValueService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductPropertyValueVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(productPropertyValueService.queryById(id));
    }

}
