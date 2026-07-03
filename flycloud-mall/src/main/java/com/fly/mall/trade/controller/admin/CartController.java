package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.trade.domain.bo.CartBo;
import com.fly.mall.api.trade.domain.vo.CartVo;
import com.fly.mall.trade.service.ICartService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 购物车 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/cart")
public class CartController extends BaseController {

    private final ICartService cartService;

    /**
     * 查询购物车分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:cart:list')")
    @GetMapping("/list")
    public R<PageVo<CartVo>> list(CartBo bo, PageBo page) {
        return R.ok(cartService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:cart:list')")
    @GetMapping("/page")
    public R<PageVo<CartVo>> page(CartBo bo, PageBo page) {
        return R.ok(cartService.queryPageList(bo, page));
    }

    /**
     * 查询所有购物车。
     */
    @GetMapping("/getList")
    public R<List<CartVo>> allList(CartBo bo) {
        return R.ok(cartService.queryList(bo));
    }

    /**
     * 获取购物车详情。
     */
    @GetMapping("/get/{id}")
    public R<CartVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(cartService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<CartVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(cartService.queryById(id));
    }

    /**
     * 新增或修改购物车。
     */
    @Log(title = "购物车", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:cart:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody CartBo bo) {
        return R.result(cartService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody CartBo bo) {
        return R.result(cartService.saveOrUpdate(bo));
    }

    /**
     * 删除购物车。
     */
    @Log(title = "购物车", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:cart:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(cartService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(cartService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}
