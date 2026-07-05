package com.fly.mall.product.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.enums.mall.ProductSpuStatusEnum;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.product.domain.bo.ProductSpuBo;
import com.fly.mall.api.product.domain.vo.AppProductSpuDetailRespVo;
import com.fly.mall.api.product.domain.vo.AppProductSpuRespVo;
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
 * @date 2026-07-02
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
    public R<PageVo<AppProductSpuRespVo>> list(ProductSpuBo bo, PageBo page) {
        bo.setStatus(ProductSpuStatusEnum.ENABLE.getStatus());
        return R.ok(productSpuService.queryAppPageList(bo, page));
    }

    /**
     * 获得商品 SPU 列表。
     */
    @GetMapping("/list-by-ids")
    public R<List<AppProductSpuRespVo>> getSpuList(@RequestParam("ids") Set<Long> ids) {
        return R.ok(productSpuService.queryAppListByIds(ids));
    }

    /**
     * 获得商品 SPU 分页。
     */
    @GetMapping("/page")
    public R<PageVo<AppProductSpuRespVo>> getSpuPage(ProductSpuBo bo, PageBo page) {
        bo.setStatus(ProductSpuStatusEnum.ENABLE.getStatus());
        return R.ok(productSpuService.queryAppPageList(bo, page));
    }

    /**
     * 获取移动端商品详情。
     */
    @GetMapping("/get/{id}")
    public R<AppProductSpuDetailRespVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        AppProductSpuDetailRespVo detail = productSpuService.queryAppDetailRespById(id);
        productBrowseHistoryService.createBrowseHistory(UserUtils.getCurUserId(), id);
        return R.ok(detail);
    }

    /**
     * 获得商品 SPU 明细。
     */
    @GetMapping("/get-detail")
    public R<AppProductSpuDetailRespVo> getSpuDetail(@RequestParam("id") Long id) {
        AppProductSpuDetailRespVo detail = productSpuService.queryAppDetailRespById(id);
        productBrowseHistoryService.createBrowseHistory(UserUtils.getCurUserId(), id);
        return R.ok(detail);
    }

}
