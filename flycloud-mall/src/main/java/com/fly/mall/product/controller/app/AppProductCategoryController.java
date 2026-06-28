package com.fly.mall.product.controller.app;

import com.fly.common.domain.model.R;
import com.fly.mall.api.domain.product.bo.ProductCategoryBo;
import com.fly.mall.api.domain.product.vo.ProductCategoryVo;
import com.fly.mall.product.service.IProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 商品分类控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/product/category")
public class AppProductCategoryController {

    private final IProductCategoryService productCategoryService;

    /**
     * 查询移动端商品分类列表。
     */
    @GetMapping("/list")
    public R<List<ProductCategoryVo>> list(ProductCategoryBo bo) {
        return R.ok(productCategoryService.queryList(bo));
    }

    /**
     * 获得商品分类列表，指定编号。
     */
    @GetMapping("/list-by-ids")
    public R<List<ProductCategoryVo>> listByIds(@RequestParam("ids") List<Long> ids) {
        return R.ok(productCategoryService.queryEnableList(ids));
    }

}
