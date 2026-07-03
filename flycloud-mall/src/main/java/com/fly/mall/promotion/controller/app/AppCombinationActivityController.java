package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.vo.AppCombinationActivityDetailRespVo;
import com.fly.mall.api.promotion.domain.vo.AppCombinationActivityRespVo;
import com.fly.mall.promotion.service.ICombinationActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 拼团活动 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/combination-activity")
public class AppCombinationActivityController {

    private final ICombinationActivityService combinationActivityService;

    /**
     * 获得拼团活动分页。
     */
    @GetMapping("/page")
    public R<PageVo<AppCombinationActivityRespVo>> page(PageBo page) {
        return R.ok(combinationActivityService.queryAppPageList(page));
    }

    /**
     * 获得拼团活动明细。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<AppCombinationActivityDetailRespVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(combinationActivityService.queryAppDetail(id));
    }

    /**
     * 根据编号列表查询拼团活动。
     */
    @GetMapping("/list-by-ids")
    public R<List<AppCombinationActivityRespVo>> listByIds(@RequestParam("ids") List<Long> ids) {
        return R.ok(combinationActivityService.queryAppListByIds(ids));
    }

}
