package com.fly.mall.trade.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.domain.trade.bo.CartBo;
import com.fly.mall.api.domain.trade.vo.AppCartListRespVo;
import com.fly.mall.trade.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 购物车 控制器。
 *
 * @author lxs
 * @date 2026-06-29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/cart")
public class AppCartController {

    private final ICartService cartService;

    /**
     * 添加购物车商品。
     */
    @PostMapping("/add")
    public R<Long> addCart(@RequestBody CartBo bo) {
        return R.ok(cartService.addCart(UserUtils.getCurUserId(), bo));
    }

    /**
     * 更新购物车商品数量。
     */
    @PutMapping("/update-count")
    public R<Void> updateCartCount(@RequestBody CartBo bo) {
        return R.ok(cartService.updateCartCount(UserUtils.getCurUserId(), bo));
    }

    /**
     * 更新购物车商品选中状态。
     */
    @PutMapping("/update-selected")
    public R<Void> updateCartSelected(@RequestBody CartBo bo) {
        return R.ok(cartService.updateCartSelected(UserUtils.getCurUserId(), bo));
    }

    /**
     * 重置购物车商品。
     */
    @PutMapping("/reset")
    public R<Void> resetCart(@RequestBody CartBo bo) {
        return R.ok(cartService.resetCart(UserUtils.getCurUserId(), bo));
    }

    /**
     * 删除购物车商品。
     */
    @DeleteMapping("/delete")
    public R<Void> deleteCart(@RequestParam("ids") List<Long> ids) {
        return R.ok(cartService.deleteCart(UserUtils.getCurUserId(), ids));
    }

    /**
     * 查询用户在购物车中的商品数量。
     */
    @GetMapping("/get-count")
    public R<Integer> getCartCount() {
        return R.ok(cartService.getCartCount(UserUtils.getCurUserId()));
    }

    /**
     * 查询用户的购物车列表。
     */
    @GetMapping("/list")
    public R<AppCartListRespVo> getCartList() {
        return R.ok(cartService.queryAppCartList(UserUtils.getCurUserId()));
    }

}
