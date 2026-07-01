package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.trade.domain.bo.AfterSaleBo;
import com.fly.mall.api.trade.domain.vo.AfterSaleVo;
import com.fly.mall.trade.service.IAfterSaleService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 移动端 - 售后单 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/after-sale")
public class AppAfterSaleController {

    private final IAfterSaleService afterSaleService;

    /**
     * 查询移动端售后单分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<AfterSaleVo>> list(AfterSaleBo bo, PageBo page) {
        return R.ok(afterSaleService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<AfterSaleVo>> page(AfterSaleBo bo, PageBo page) {
        return R.ok(afterSaleService.queryPageList(bo, page));
    }

    /**
     * 获取移动端售后单详情。
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
     * 创建售后申请。
     */
    @PostMapping("/create")
    public R<Void> create(@RequestBody AfterSaleBo bo) {
        bo.setUserId(UserUtils.getCurUserId());
        bo.setStatus(10);
        return R.ok(afterSaleService.saveOrUpdate(bo));
    }

    /**
     * 取消售后申请。
     */
    @DeleteMapping("/cancel")
    public R<Void> cancel(@RequestParam("id") Long id) {
        AfterSaleBo bo = new AfterSaleBo();
        bo.setId(id);
        bo.setStatus(62);
        return R.ok(afterSaleService.saveOrUpdate(bo));
    }

    /**
     * 用户填写退货物流。
     */
    @PutMapping("/delivery")
    public R<Void> delivery(@RequestBody AfterSaleBo bo) {
        bo.setStatus(30);
        bo.setDeliveryTime(LocalDateTime.now());
        return R.ok(afterSaleService.saveOrUpdate(bo));
    }

}
