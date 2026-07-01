package com.fly.mall.product.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.product.domain.bo.ProductPropertyBo;
import com.fly.mall.api.product.domain.vo.ProductPropertyVo;
import com.fly.mall.product.service.IProductPropertyService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 商品属性 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/product/product-property")
public class AppProductPropertyController {

    private final IProductPropertyService productPropertyService;

    /**
     * 查询移动端商品属性分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<ProductPropertyVo>> list(ProductPropertyBo bo, PageBo page) {
        return R.ok(productPropertyService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<ProductPropertyVo>> page(ProductPropertyBo bo, PageBo page) {
        return R.ok(productPropertyService.queryPageList(bo, page));
    }

    /**
     * 获取移动端商品属性详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductPropertyVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productPropertyService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductPropertyVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(productPropertyService.queryById(id));
    }

}
