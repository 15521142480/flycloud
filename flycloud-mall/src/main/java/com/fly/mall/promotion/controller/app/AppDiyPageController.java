package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.DiyPageBo;
import com.fly.mall.api.promotion.domain.vo.AppDiyPagePropertyRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyPageRespVo;
import com.fly.mall.promotion.service.IDiyPageService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 装修页面 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/diy-page")
public class AppDiyPageController {

    private final IDiyPageService diyPageService;

    /**
     * 获取移动端装修页面详情。
     */
    @GetMapping("/get")
    public R<AppDiyPagePropertyRespVo> get(@RequestParam("id") Long id) {
        return R.ok(diyPageService.queryAppPropertyRespById(id));
    }

    /**
     * 查询移动端装修页面分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<DiyPageRespVo>> list(DiyPageBo bo, PageBo page) {
        return R.ok(diyPageService.queryRespPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<DiyPageRespVo>> page(DiyPageBo bo, PageBo page) {
        return R.ok(diyPageService.queryRespPageList(bo, page));
    }

    /**
     * 获取移动端装修页面详情。
     */
    @GetMapping("/get/{id}")
    public R<AppDiyPagePropertyRespVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(diyPageService.queryAppPropertyRespById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<AppDiyPagePropertyRespVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(diyPageService.queryAppPropertyRespById(id));
    }

}
