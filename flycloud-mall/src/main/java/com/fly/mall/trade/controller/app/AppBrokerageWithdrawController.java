package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.trade.domain.bo.BrokerageWithdrawBo;
import com.fly.mall.api.trade.domain.vo.BrokerageWithdrawVo;
import com.fly.mall.trade.service.IBrokerageWithdrawService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 佣金提现 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/brokerage-withdraw")
public class AppBrokerageWithdrawController {

    private final IBrokerageWithdrawService brokerageWithdrawService;

    /**
     * 查询移动端佣金提现分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<BrokerageWithdrawVo>> list(BrokerageWithdrawBo bo, PageBo page) {
        return R.ok(brokerageWithdrawService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<BrokerageWithdrawVo>> page(BrokerageWithdrawBo bo, PageBo page) {
        return R.ok(brokerageWithdrawService.queryPageList(bo, page));
    }

    /**
     * 获取移动端佣金提现详情。
     */
    @GetMapping("/get/{id}")
    public R<BrokerageWithdrawVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(brokerageWithdrawService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<BrokerageWithdrawVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(brokerageWithdrawService.queryById(id));
    }

    /**
     * 创建佣金提现申请。
     */
    @PostMapping("/create")
    public R<Void> create(@RequestBody BrokerageWithdrawBo bo) {
        bo.setUserId(UserUtils.getCurUserId());
        bo.setStatus(0);
        return R.ok(brokerageWithdrawService.saveOrUpdate(bo));
    }

}
