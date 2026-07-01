package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.trade.domain.bo.BrokerageUserBo;
import com.fly.mall.api.trade.domain.vo.BrokerageUserVo;
import com.fly.mall.trade.service.IBrokerageUserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;

/**
 * 移动端 - 分销用户 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/brokerage-user")
public class AppBrokerageUserController {

    private final IBrokerageUserService brokerageUserService;

    /**
     * 查询移动端分销用户分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<BrokerageUserVo>> list(BrokerageUserBo bo, PageBo page) {
        return R.ok(brokerageUserService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<BrokerageUserVo>> page(BrokerageUserBo bo, PageBo page) {
        return R.ok(brokerageUserService.queryPageList(bo, page));
    }

    /**
     * 获取移动端分销用户详情。
     */
    @GetMapping("/get/{id}")
    public R<BrokerageUserVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(brokerageUserService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<BrokerageUserVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(brokerageUserService.queryById(id));
    }

    /**
     * 绑定推广人。
     */
    @PutMapping("/bind")
    public R<Void> bind(@RequestBody BrokerageUserBo bo) {
        bo.setId(UserUtils.getCurUserId());
        bo.setBindUserTime(LocalDateTime.now());
        return R.ok(brokerageUserService.saveOrUpdate(bo));
    }

    /**
     * 获得分销用户汇总。
     */
    @GetMapping("/get-summary")
    public R<Map<String, Long>> getSummary() {
        BrokerageUserBo bo = new BrokerageUserBo();
        bo.setBindUserId(UserUtils.getCurUserId());
        return R.ok(Map.of("childCount", (long) brokerageUserService.queryList(bo).size()));
    }

    /**
     * 获得下级用户汇总分页。
     */
    @GetMapping("/child-summary-page")
    public R<PageVo<BrokerageUserVo>> childSummaryPage(BrokerageUserBo bo, PageBo page) {
        bo.setBindUserId(UserUtils.getCurUserId());
        return R.ok(brokerageUserService.queryPageList(bo, page));
    }

    /**
     * 获得佣金排行分页。
     */
    @GetMapping({"/rank-page-by-price", "/rank-page-by-user-count"})
    public R<PageVo<BrokerageUserVo>> rankPage(BrokerageUserBo bo, PageBo page) {
        return R.ok(brokerageUserService.queryPageList(bo, page));
    }

    /**
     * 获得当前用户佣金排行。
     */
    @GetMapping("/get-rank-by-price")
    public R<Long> getRankByPrice() {
        java.util.List<BrokerageUserVo> users = brokerageUserService.queryList(new BrokerageUserBo()).stream()
                .sorted(Comparator.comparing(BrokerageUserVo::getBrokeragePrice,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
        Long userId = UserUtils.getCurUserId();
        for (int i = 0; i < users.size(); i++) {
            if (userId.equals(users.get(i).getId())) {
                return R.ok((long) i + 1);
            }
        }
        return R.ok(0L);
    }

}
