package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.DiyTemplateBo;
import com.fly.mall.api.domain.promotion.vo.DiyTemplateVo;
import com.fly.mall.promotion.service.IDiyTemplateService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 装修模板 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/diy-template")
public class AppDiyTemplateController {

    private final IDiyTemplateService diyTemplateService;

    /**
     * 查询移动端装修模板分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<DiyTemplateVo>> list(DiyTemplateBo bo, PageBo page) {
        return R.ok(diyTemplateService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<DiyTemplateVo>> page(DiyTemplateBo bo, PageBo page) {
        return R.ok(diyTemplateService.queryPageList(bo, page));
    }

    /**
     * 获取移动端装修模板详情。
     */
    @GetMapping("/get/{id}")
    public R<DiyTemplateVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(diyTemplateService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<DiyTemplateVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(diyTemplateService.queryById(id));
    }

}
