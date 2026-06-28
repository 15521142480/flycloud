package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.trade.bo.BrokerageUserBo;
import com.fly.mall.api.domain.trade.vo.BrokerageUserVo;
import com.fly.mall.trade.service.IBrokerageUserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/get-detail")
    public R<BrokerageUserVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(brokerageUserService.queryById(id));
    }

}
