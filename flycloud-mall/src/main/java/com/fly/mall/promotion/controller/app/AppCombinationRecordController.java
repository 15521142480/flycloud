package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.CombinationRecordBo;
import com.fly.mall.api.promotion.domain.vo.CombinationRecordVo;
import com.fly.mall.promotion.service.ICombinationRecordService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 移动端 - 拼团记录 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/combination-record")
public class AppCombinationRecordController {

    private final ICombinationRecordService combinationRecordService;

    /**
     * 查询移动端拼团记录分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<CombinationRecordVo>> list(CombinationRecordBo bo, PageBo page) {
        return R.ok(combinationRecordService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<CombinationRecordVo>> page(CombinationRecordBo bo, PageBo page) {
        return R.ok(combinationRecordService.queryPageList(bo, page));
    }

    /**
     * 获取移动端拼团记录详情。
     */
    @GetMapping("/get/{id}")
    public R<CombinationRecordVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(combinationRecordService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<CombinationRecordVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(combinationRecordService.queryById(id));
    }

    /**
     * 获得团长拼团记录列表。
     */
    @GetMapping("/get-head-list")
    public R<java.util.List<CombinationRecordVo>> getHeadList(@RequestParam("activityId") Long activityId) {
        CombinationRecordBo bo = new CombinationRecordBo();
        bo.setActivityId(activityId);
        return R.ok(combinationRecordService.queryList(bo).stream()
                .filter(item -> item.getHeadId() == null || item.getHeadId().equals(item.getId())).toList());
    }

    /**
     * 获得拼团记录汇总。
     */
    @GetMapping("/get-summary")
    public R<Map<String, Long>> getSummary(@RequestParam(value = "activityId", required = false) Long activityId) {
        CombinationRecordBo bo = new CombinationRecordBo();
        bo.setActivityId(activityId);
        return R.ok(Map.of("count", (long) combinationRecordService.queryList(bo).size()));
    }

}
