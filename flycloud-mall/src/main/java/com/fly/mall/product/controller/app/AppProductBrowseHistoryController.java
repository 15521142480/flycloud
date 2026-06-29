package com.fly.mall.product.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.product.domain.bo.ProductBrowseHistoryBo;
import com.fly.mall.api.product.domain.vo.ProductBrowseHistoryVo;
import com.fly.mall.product.service.IProductBrowseHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 商品浏览记录 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/product/browse-history")
public class AppProductBrowseHistoryController {

    private final IProductBrowseHistoryService productBrowseHistoryService;

    /**
     * 删除商品浏览记录。
     */
    @DeleteMapping("/delete")
    public R<Void> deleteBrowseHistory(@RequestBody ProductBrowseHistoryBo bo) {
        return R.ok(productBrowseHistoryService.hideUserBrowseHistory(UserUtils.getCurUserId(), List.of(bo.getSpuId())));
    }

    /**
     * 清空商品浏览记录。
     */
    @DeleteMapping("/clean")
    public R<Void> cleanBrowseHistory() {
        return R.ok(productBrowseHistoryService.hideUserBrowseHistory(UserUtils.getCurUserId(), null));
    }

    /**
     * 获得商品浏览记录分页。
     */
    @GetMapping("/page")
    public R<PageVo<ProductBrowseHistoryVo>> getBrowseHistoryPage(ProductBrowseHistoryBo bo, PageBo page) {
        return R.ok(productBrowseHistoryService.queryUserBrowseHistoryPage(UserUtils.getCurUserId(), bo, page));
    }

}
