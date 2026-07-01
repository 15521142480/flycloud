package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.trade.domain.bo.BrokerageRecordBo;
import com.fly.mall.api.trade.domain.vo.BrokerageRecordVo;
import com.fly.mall.trade.service.IBrokerageRecordService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 佣金记录 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/brokerage-record")
public class BrokerageRecordController extends BaseController {

    private final IBrokerageRecordService brokerageRecordService;

    /**
     * 查询佣金记录分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-record:list')")
    @GetMapping("/list")
    public R<PageVo<BrokerageRecordVo>> list(BrokerageRecordBo bo, PageBo page) {
        return R.ok(brokerageRecordService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-record:list')")
    @GetMapping("/page")
    public R<PageVo<BrokerageRecordVo>> page(BrokerageRecordBo bo, PageBo page) {
        return R.ok(brokerageRecordService.queryPageList(bo, page));
    }

    /**
     * 查询所有佣金记录。
     */
    @GetMapping("/getList")
    public R<List<BrokerageRecordVo>> allList(BrokerageRecordBo bo) {
        return R.ok(brokerageRecordService.queryList(bo));
    }

    /**
     * 获取佣金记录详情。
     */
    @GetMapping("/get/{id}")
    public R<BrokerageRecordVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(brokerageRecordService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<BrokerageRecordVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(brokerageRecordService.queryById(id));
    }

    /**
     * 新增或修改佣金记录。
     */
    @Log(title = "佣金记录", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-record:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody BrokerageRecordBo bo) {
        return R.ok(brokerageRecordService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody BrokerageRecordBo bo) {
        return R.ok(brokerageRecordService.saveOrUpdate(bo));
    }

    /**
     * 删除佣金记录。
     */
    @Log(title = "佣金记录", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-record:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(brokerageRecordService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(brokerageRecordService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}
