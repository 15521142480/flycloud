package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.SeckillProductBo;
import com.fly.mall.api.promotion.domain.vo.SeckillProductVo;
import com.fly.mall.promotion.service.ISeckillProductService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 秒杀商品 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/seckill-product")
public class AppSeckillProductController {

    private final ISeckillProductService seckillProductService;

    /**
     * 查询移动端秒杀商品分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<SeckillProductVo>> list(SeckillProductBo bo, PageBo page) {
        return R.ok(seckillProductService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<SeckillProductVo>> page(SeckillProductBo bo, PageBo page) {
        return R.ok(seckillProductService.queryPageList(bo, page));
    }

    /**
     * 获取移动端秒杀商品详情。
     */
    @GetMapping("/get/{id}")
    public R<SeckillProductVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(seckillProductService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<SeckillProductVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(seckillProductService.queryById(id));
    }

}
