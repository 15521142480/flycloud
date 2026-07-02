package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.BannerBo;
import com.fly.mall.api.promotion.domain.vo.BannerVo;
import com.fly.mall.promotion.service.IBannerService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 轮播图 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/banner")
public class AppBannerController {

    private final IBannerService bannerService;

    /**
     * 查询移动端轮播图分页列表。
     */
    @GetMapping("/list")
    public R<List<BannerVo>> list(@RequestParam("position") Integer position) {
        return R.ok(bannerService.queryListByPosition(position));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<BannerVo>> page(BannerBo bo, PageBo page) {
        return R.ok(bannerService.queryPageList(bo, page));
    }

    /**
     * 获取移动端轮播图详情。
     */
    @GetMapping("/get/{id}")
    public R<BannerVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(bannerService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<BannerVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(bannerService.queryById(id));
    }

    /**
     * 增加轮播图浏览次数。
     */
    @PutMapping("/add-browse-count")
    public R<Boolean> addBrowseCount(@RequestParam("id") Long id) {
        return R.ok(bannerService.addBrowseCount(id));
    }

}
