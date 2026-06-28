package com.fly.mall.product.controller.app;

import com.fly.common.domain.model.R;
import com.fly.mall.api.domain.product.bo.ProductBrandBo;
import com.fly.mall.api.domain.product.vo.ProductBrandVo;
import com.fly.mall.product.service.IProductBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 商品品牌控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/product/brand")
public class AppProductBrandController {

    private final IProductBrandService productBrandService;

    /**
     * 查询移动端商品品牌列表。
     */
    @GetMapping("/list")
    public R<List<ProductBrandVo>> list(ProductBrandBo bo) {
        return R.ok(productBrandService.queryList(bo));
    }

}
