package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.CombinationProductBo;
import com.fly.mall.api.promotion.domain.vo.CombinationProductVo;
import com.fly.mall.promotion.service.ICombinationProductService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 拼团商品 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/combination-product")
public class AppCombinationProductController {

    private final ICombinationProductService combinationProductService;

    /**
     * 查询移动端拼团商品分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<CombinationProductVo>> list(CombinationProductBo bo, PageBo page) {
        return R.ok(combinationProductService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<CombinationProductVo>> page(CombinationProductBo bo, PageBo page) {
        return R.ok(combinationProductService.queryPageList(bo, page));
    }

    /**
     * 获取移动端拼团商品详情。
     */
    @GetMapping("/get/{id}")
    public R<CombinationProductVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(combinationProductService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<CombinationProductVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(combinationProductService.queryById(id));
    }

}
