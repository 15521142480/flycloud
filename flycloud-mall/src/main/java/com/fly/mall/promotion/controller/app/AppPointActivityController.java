package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.PointActivityBo;
import com.fly.mall.api.promotion.domain.vo.PointActivityVo;
import com.fly.mall.promotion.service.IPointActivityService;
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
 * 移动端 - 积分商城活动 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/point-activity")
public class AppPointActivityController {

    private final IPointActivityService pointActivityService;

    /**
     * 查询移动端积分商城活动分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<PointActivityVo>> list(PointActivityBo bo, PageBo page) {
        return R.ok(pointActivityService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<PointActivityVo>> page(PointActivityBo bo, PageBo page) {
        return R.ok(pointActivityService.queryPageList(bo, page));
    }

    /**
     * 获取移动端积分商城活动详情。
     */
    @GetMapping("/get/{id}")
    public R<PointActivityVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(pointActivityService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<PointActivityVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(pointActivityService.queryById(id));
    }

    /**
     * 根据编号列表查询积分商城活动。
     */
    @GetMapping("/list-by-ids")
    public R<List<PointActivityVo>> listByIds(@RequestParam("ids") List<Long> ids) {
        return R.ok(pointActivityService.queryList(new PointActivityBo()).stream()
                .filter(item -> ids.contains(item.getId())).toList());
    }

}
