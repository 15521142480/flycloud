package com.fly.mall.product.controller.app;

import com.fly.common.domain.model.R;
import com.fly.mall.api.product.domain.vo.ProductCategoryVo;
import com.fly.mall.product.service.IProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 移动端 - 商品分类兼容控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/category")
public class AppCategoryController {

    private final IProductCategoryService productCategoryService;

    /**
     * 获得启用的商品分类列表。
     */
    @GetMapping("/list")
    public R<List<ProductCategoryVo>> getProductCategoryList() {
        return R.ok(productCategoryService.queryEnableList(null));
    }

    /**
     * 获得指定编号的启用商品分类列表。
     */
    @GetMapping("/list-by-ids")
    public R<List<ProductCategoryVo>> getProductCategoryList(@RequestParam("ids") List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return R.ok(Collections.emptyList());
        }
        return R.ok(productCategoryService.queryEnableList(ids));
    }

}
