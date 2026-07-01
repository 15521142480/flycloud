package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.SeckillActivityBo;
import com.fly.mall.api.promotion.domain.vo.SeckillActivityVo;
import com.fly.mall.promotion.service.ISeckillActivityService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 秒杀活动 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/seckill-activity")
public class AppSeckillActivityController {

    private final ISeckillActivityService seckillActivityService;

    /**
     * 查询移动端秒杀活动分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<SeckillActivityVo>> list(SeckillActivityBo bo, PageBo page) {
        return R.ok(seckillActivityService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<SeckillActivityVo>> page(SeckillActivityBo bo, PageBo page) {
        return R.ok(seckillActivityService.queryPageList(bo, page));
    }

    /**
     * 获取移动端秒杀活动详情。
     */
    @GetMapping("/get/{id}")
    public R<SeckillActivityVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(seckillActivityService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<SeckillActivityVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(seckillActivityService.queryById(id));
    }

    /**
     * 根据编号列表查询秒杀活动。
     */
    @GetMapping("/list-by-ids")
    public R<List<SeckillActivityVo>> listByIds(@RequestParam("ids") List<Long> ids) {
        return R.ok(seckillActivityService.queryList(new SeckillActivityBo()).stream()
                .filter(item -> ids.contains(item.getId())).toList());
    }

    /**
     * 获得当前秒杀活动。
     */
    @GetMapping("/get-now")
    public R<SeckillActivityVo> getNow() {
        return R.ok(seckillActivityService.queryList(new SeckillActivityBo()).stream().findFirst().orElse(null));
    }

}
