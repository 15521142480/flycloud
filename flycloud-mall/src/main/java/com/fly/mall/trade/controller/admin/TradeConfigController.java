package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.domain.trade.bo.TradeConfigBo;
import com.fly.mall.api.domain.trade.vo.TradeConfigVo;
import com.fly.mall.trade.service.ITradeConfigService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 交易配置 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/trade-config")
public class TradeConfigController extends BaseController {

    private final ITradeConfigService tradeConfigService;

    /**
     * 查询交易配置分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-config:list')")
    @GetMapping("/list")
    public R<PageVo<TradeConfigVo>> list(TradeConfigBo bo, PageBo page) {
        return R.ok(tradeConfigService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:trade-config:list')")
    @GetMapping("/page")
    public R<PageVo<TradeConfigVo>> page(TradeConfigBo bo, PageBo page) {
        return R.ok(tradeConfigService.queryPageList(bo, page));
    }

    /**
     * 查询所有交易配置。
     */
    @GetMapping("/getList")
    public R<List<TradeConfigVo>> allList(TradeConfigBo bo) {
        return R.ok(tradeConfigService.queryList(bo));
    }

    /**
     * 获取交易配置详情。
     */
    @GetMapping("/get/{id}")
    public R<TradeConfigVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(tradeConfigService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<TradeConfigVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(tradeConfigService.queryById(id));
    }

    /**
     * 新增或修改交易配置。
     */
    @Log(title = "交易配置", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-config:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody TradeConfigBo bo) {
        return R.ok(tradeConfigService.saveOrUpdate(bo));
    }

    /**
     * 删除交易配置。
     */
    @Log(title = "交易配置", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-config:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(tradeConfigService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
