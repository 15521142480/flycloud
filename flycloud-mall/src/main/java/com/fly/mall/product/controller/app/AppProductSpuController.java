package com.fly.mall.product.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.domain.product.bo.ProductSpuBo;
import com.fly.mall.api.domain.product.vo.ProductSpuVo;
import com.fly.mall.product.service.IProductBrowseHistoryService;
import com.fly.mall.product.service.IProductSpuService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 移动端 - 商品 SPU 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/product/spu")
public class AppProductSpuController {

    private final IProductSpuService productSpuService;
    private final IProductBrowseHistoryService productBrowseHistoryService;

    /**
     * 查询移动端商品分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<ProductSpuVo>> list(ProductSpuBo bo, PageBo page) {
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        return R.ok(productSpuService.queryPageList(bo, page));
    }

    /**
     * 获得商品 SPU 列表。
     */
    @GetMapping("/list-by-ids")
    public R<List<ProductSpuVo>> getSpuList(@RequestParam("ids") Set<Long> ids) {
        return R.ok(productSpuService.queryListByIds(ids));
    }

    /**
     * 获得商品 SPU 分页。
     */
    @GetMapping("/page")
    public R<PageVo<ProductSpuVo>> getSpuPage(ProductSpuBo bo, PageBo page) {
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        return R.ok(productSpuService.queryPageList(bo, page));
    }

    /**
     * 获取移动端商品详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductSpuVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        ProductSpuVo detail = productSpuService.queryAppDetailById(id);
        productBrowseHistoryService.createBrowseHistory(UserUtils.getCurUserId(), id);
        return R.ok(detail);
    }

    /**
     * 获得商品 SPU 明细。
     */
    @GetMapping("/get-detail")
    public R<ProductSpuVo> getSpuDetail(@RequestParam("id") Long id) {
        ProductSpuVo detail = productSpuService.queryAppDetailById(id);
        productBrowseHistoryService.createBrowseHistory(UserUtils.getCurUserId(), id);
        return R.ok(detail);
    }

}
