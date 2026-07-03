package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.trade.domain.bo.AfterSaleBo;
import com.fly.mall.api.trade.domain.vo.AfterSaleVo;
import com.fly.mall.trade.service.IAfterSaleService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 售后单 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/after-sale")
public class AfterSaleController extends BaseController {

    private final IAfterSaleService afterSaleService;

    /**
     * 查询售后单分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:list')")
    @GetMapping("/list")
    public R<PageVo<AfterSaleVo>> list(AfterSaleBo bo, PageBo page) {
        return R.ok(afterSaleService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:list')")
    @GetMapping("/page")
    public R<PageVo<AfterSaleVo>> page(AfterSaleBo bo, PageBo page) {
        return R.ok(afterSaleService.queryPageList(bo, page));
    }

    /**
     * 查询所有售后单。
     */
    @GetMapping("/getList")
    public R<List<AfterSaleVo>> allList(AfterSaleBo bo) {
        return R.ok(afterSaleService.queryList(bo));
    }

    /**
     * 获取售后单详情。
     */
    @GetMapping("/get/{id}")
    public R<AfterSaleVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(afterSaleService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<AfterSaleVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(afterSaleService.queryById(id));
    }

    /**
     * 新增或修改售后单。
     */
    @Log(title = "售后单", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody AfterSaleBo bo) {
        return R.result(afterSaleService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody AfterSaleBo bo) {
        return R.result(afterSaleService.saveOrUpdate(bo));
    }

    /**
     * 同意售后申请。
     */
    @PutMapping("/agree")
    public R<Void> agree(@RequestBody(required = false) AfterSaleBo bo, @RequestParam(value = "id", required = false) Long id) {
        Long afterSaleId = id != null ? id : bo.getId();
        return R.result(afterSaleService.agreeAfterSale(UserUtils.getCurUserId(), afterSaleId));
    }

    /**
     * 拒绝售后申请。
     */
    @PutMapping("/disagree")
    public R<Void> disagree(@RequestBody AfterSaleBo bo) {
        return R.result(afterSaleService.disagreeAfterSale(UserUtils.getCurUserId(), bo));
    }

    /**
     * 确认收到退货。
     */
    @PutMapping("/receive")
    public R<Void> receive(@RequestBody(required = false) AfterSaleBo bo, @RequestParam(value = "id", required = false) Long id) {
        Long afterSaleId = id != null ? id : bo.getId();
        return R.result(afterSaleService.receiveAfterSale(UserUtils.getCurUserId(), afterSaleId));
    }

    /**
     * 售后退款。
     */
    @PutMapping("/refund")
    public R<Void> refund(@RequestBody(required = false) AfterSaleBo bo, @RequestParam(value = "id", required = false) Long id) {
        Long afterSaleId = id != null ? id : bo.getId();
        return R.result(afterSaleService.refundAfterSale(UserUtils.getCurUserId(), afterSaleId));
    }

    /**
     * 拒绝收货。
     */
    @PutMapping("/refuse")
    public R<Void> refuse(@RequestBody AfterSaleBo bo) {
        return R.result(afterSaleService.refuseAfterSale(UserUtils.getCurUserId(), bo));
    }

    /**
     * 支付退款回调后更新售后单。
     */
    @PostMapping("/update-refunded")
    public R<Void> updateRefunded(@RequestBody AfterSaleBo bo) {
        return R.result(afterSaleService.updateAfterSaleRefunded(bo.getId(), bo.getOrderId(), bo.getPayRefundId()));
    }

    /**
     * 删除售后单。
     */
    @Log(title = "售后单", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(afterSaleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(afterSaleService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}
