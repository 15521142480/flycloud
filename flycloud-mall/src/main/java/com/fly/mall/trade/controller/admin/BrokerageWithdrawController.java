package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.trade.domain.bo.BrokerageWithdrawBo;
import com.fly.mall.api.trade.domain.vo.BrokerageWithdrawVo;
import com.fly.mall.trade.service.IBrokerageWithdrawService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;

/**
 * 管理后台 - 佣金提现 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/brokerage-withdraw")
public class BrokerageWithdrawController extends BaseController {

    private final IBrokerageWithdrawService brokerageWithdrawService;

    /**
     * 查询佣金提现分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-withdraw:list')")
    @GetMapping("/list")
    public R<PageVo<BrokerageWithdrawVo>> list(BrokerageWithdrawBo bo, PageBo page) {
        return R.ok(brokerageWithdrawService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-withdraw:list')")
    @GetMapping("/page")
    public R<PageVo<BrokerageWithdrawVo>> page(BrokerageWithdrawBo bo, PageBo page) {
        return R.ok(brokerageWithdrawService.queryPageList(bo, page));
    }

    /**
     * 查询所有佣金提现。
     */
    @GetMapping("/getList")
    public R<List<BrokerageWithdrawVo>> allList(BrokerageWithdrawBo bo) {
        return R.ok(brokerageWithdrawService.queryList(bo));
    }

    /**
     * 获取佣金提现详情。
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
     * 新增或修改佣金提现。
     */
    @Log(title = "佣金提现", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-withdraw:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody BrokerageWithdrawBo bo) {
        return R.result(brokerageWithdrawService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody BrokerageWithdrawBo bo) {
        return R.result(brokerageWithdrawService.saveOrUpdate(bo));
    }

    /**
     * 同意佣金提现。
     */
    @PutMapping("/approve")
    public R<Void> approve(@RequestBody BrokerageWithdrawBo bo) {
        bo.setStatus(10);
        bo.setAuditTime(LocalDateTime.now());
        return R.result(brokerageWithdrawService.saveOrUpdate(bo));
    }

    /**
     * 拒绝佣金提现。
     */
    @PutMapping("/reject")
    public R<Void> reject(@RequestBody BrokerageWithdrawBo bo) {
        bo.setStatus(20);
        bo.setAuditTime(LocalDateTime.now());
        return R.result(brokerageWithdrawService.saveOrUpdate(bo));
    }

    /**
     * 转账回调后更新佣金提现。
     */
    @PostMapping("/update-transferred")
    public R<Void> updateTransferred(@RequestBody BrokerageWithdrawBo bo) {
        bo.setStatus(30);
        bo.setTransferTime(LocalDateTime.now());
        return R.result(brokerageWithdrawService.saveOrUpdate(bo));
    }

    /**
     * 删除佣金提现。
     */
    @Log(title = "佣金提现", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-withdraw:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(brokerageWithdrawService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(brokerageWithdrawService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}
