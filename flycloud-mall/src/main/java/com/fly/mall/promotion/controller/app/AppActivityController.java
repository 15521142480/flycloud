package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.enums.StatusEnum;
import com.fly.mall.api.promotion.domain.bo.BargainActivityBo;
import com.fly.mall.api.promotion.domain.bo.CombinationActivityBo;
import com.fly.mall.api.promotion.domain.bo.SeckillActivityBo;
import com.fly.mall.api.promotion.domain.vo.AppActivityRespVo;
import com.fly.mall.api.promotion.domain.vo.BargainActivityVo;
import com.fly.mall.api.promotion.domain.vo.CombinationActivityVo;
import com.fly.mall.api.promotion.domain.vo.SeckillActivityVo;
import com.fly.mall.promotion.service.IBargainActivityService;
import com.fly.mall.promotion.service.ICombinationActivityService;
import com.fly.mall.promotion.service.ISeckillActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 移动端 - 营销活动聚合控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping({"/promotion/activity", "/app/promotion/activity"})
public class AppActivityController {

    private static final int PROMOTION_TYPE_SECKILL = 1;
    private static final int PROMOTION_TYPE_BARGAIN = 2;
    private static final int PROMOTION_TYPE_COMBINATION = 3;

    private final ICombinationActivityService combinationActivityService;
    private final ISeckillActivityService seckillActivityService;
    private final IBargainActivityService bargainActivityService;

    /**
     * 获得单个商品正在进行中的拼团、秒杀、砍价活动信息。
     */
    @GetMapping("/list-by-spu-id")
    public R<List<AppActivityRespVo>> getActivityListBySpuId(@RequestParam("spuId") Long spuId) {
        List<AppActivityRespVo> result = new ArrayList<>();
        findCombination(spuId).stream().findFirst().ifPresent(activity -> result.add(new AppActivityRespVo(
                activity.getId(), PROMOTION_TYPE_COMBINATION, activity.getName(), activity.getSpuId(),
                activity.getStartTime(), activity.getEndTime())));
        findSeckill(spuId).stream().findFirst().ifPresent(activity -> result.add(new AppActivityRespVo(
                activity.getId(), PROMOTION_TYPE_SECKILL, activity.getName(), activity.getSpuId(),
                activity.getStartTime(), activity.getEndTime())));
        findBargain(spuId).stream().findFirst().ifPresent(activity -> result.add(new AppActivityRespVo(
                activity.getId(), PROMOTION_TYPE_BARGAIN, activity.getName(), activity.getSpuId(),
                activity.getStartTime(), activity.getEndTime())));
        return R.ok(result);
    }

    private List<CombinationActivityVo> findCombination(Long spuId) {
        CombinationActivityBo bo = new CombinationActivityBo();
        bo.setSpuId(spuId);
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        return combinationActivityService.queryList(bo).stream()
                .filter(this::isInProgress)
                .sorted(Comparator.comparing(CombinationActivityVo::getStartTime, Comparator.nullsLast(LocalDateTime::compareTo)))
                .toList();
    }

    private List<SeckillActivityVo> findSeckill(Long spuId) {
        SeckillActivityBo bo = new SeckillActivityBo();
        bo.setSpuId(spuId);
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        return seckillActivityService.queryList(bo).stream()
                .filter(this::isInProgress)
                .sorted(Comparator.comparing(SeckillActivityVo::getStartTime, Comparator.nullsLast(LocalDateTime::compareTo)))
                .toList();
    }

    private List<BargainActivityVo> findBargain(Long spuId) {
        BargainActivityBo bo = new BargainActivityBo();
        bo.setSpuId(spuId);
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        return bargainActivityService.queryList(bo).stream()
                .filter(this::isInProgress)
                .sorted(Comparator.comparing(BargainActivityVo::getStartTime, Comparator.nullsLast(LocalDateTime::compareTo)))
                .toList();
    }

    private boolean isInProgress(CombinationActivityVo activity) {
        return isInProgress(activity.getStartTime(), activity.getEndTime());
    }

    private boolean isInProgress(SeckillActivityVo activity) {
        return isInProgress(activity.getStartTime(), activity.getEndTime());
    }

    private boolean isInProgress(BargainActivityVo activity) {
        return isInProgress(activity.getStartTime(), activity.getEndTime());
    }

    private boolean isInProgress(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        return (startTime == null || !startTime.isAfter(now))
                && (endTime == null || !endTime.isBefore(now));
    }

}
