package com.fly.mall.product.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.product.domain.bo.ProductFavoriteBo;
import com.fly.mall.api.product.domain.vo.ProductFavoriteVo;
import com.fly.mall.product.service.IProductFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 商品收藏兼容控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/favorite")
public class AppFavoriteController {

    private final IProductFavoriteService productFavoriteService;

    /**
     * 添加商品收藏。
     */
    @PostMapping("/create")
    public R<Long> createFavorite(@RequestBody ProductFavoriteBo bo) {
        return R.ok(productFavoriteService.createFavorite(UserUtils.getCurUserId(), bo.getSpuId()));
    }

    /**
     * 取消单个商品收藏。
     */
    @DeleteMapping("/delete")
    public R<Boolean> deleteFavorite(@RequestBody ProductFavoriteBo bo) {
        return R.ok(productFavoriteService.deleteFavorite(UserUtils.getCurUserId(), bo.getSpuId()));
    }

    /**
     * 获得商品收藏分页。
     */
    @GetMapping("/page")
    public R<PageVo<ProductFavoriteVo>> getFavoritePage(ProductFavoriteBo bo, PageBo pageBo) {
        return R.ok(productFavoriteService.queryUserFavoritePage(UserUtils.getCurUserId(), bo, pageBo));
    }

    /**
     * 检查是否收藏过商品。
     */
    @GetMapping({"/exits", "/exists"})
    public R<Boolean> isFavoriteExists(ProductFavoriteBo bo) {
        return R.ok(productFavoriteService.isFavoriteExists(UserUtils.getCurUserId(), bo.getSpuId()));
    }

    /**
     * 获得商品收藏数量。
     */
    @GetMapping("/get-count")
    public R<Long> getFavoriteCount() {
        return R.ok(productFavoriteService.getFavoriteCount(UserUtils.getCurUserId()));
    }

}
