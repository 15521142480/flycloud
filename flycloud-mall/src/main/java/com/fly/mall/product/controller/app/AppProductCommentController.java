package com.fly.mall.product.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.product.domain.bo.ProductCommentBo;
import com.fly.mall.api.product.domain.vo.ProductCommentVo;
import com.fly.mall.product.service.IProductCommentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 商品评价 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/product/product-comment")
public class AppProductCommentController {

    private final IProductCommentService productCommentService;

    /**
     * 查询移动端商品评价分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<ProductCommentVo>> list(ProductCommentBo bo, PageBo page) {
        return R.ok(productCommentService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<ProductCommentVo>> page(ProductCommentBo bo, PageBo page) {
        return R.ok(productCommentService.queryPageList(bo, page));
    }

    /**
     * 获取移动端商品评价详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductCommentVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productCommentService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductCommentVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(productCommentService.queryById(id));
    }

}
