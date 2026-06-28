package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.domain.trade.bo.BrokerageUserBo;
import com.fly.mall.api.domain.trade.vo.BrokerageUserVo;
import com.fly.mall.trade.service.IBrokerageUserService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 分销用户 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/brokerage-user")
public class BrokerageUserController extends BaseController {

    private final IBrokerageUserService brokerageUserService;

    /**
     * 查询分销用户分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-user:list')")
    @GetMapping("/list")
    public R<PageVo<BrokerageUserVo>> list(BrokerageUserBo bo, PageBo page) {
        return R.ok(brokerageUserService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-user:list')")
    @GetMapping("/page")
    public R<PageVo<BrokerageUserVo>> page(BrokerageUserBo bo, PageBo page) {
        return R.ok(brokerageUserService.queryPageList(bo, page));
    }

    /**
     * 查询所有分销用户。
     */
    @GetMapping("/getList")
    public R<List<BrokerageUserVo>> allList(BrokerageUserBo bo) {
        return R.ok(brokerageUserService.queryList(bo));
    }

    /**
     * 获取分销用户详情。
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

    /**
     * 新增或修改分销用户。
     */
    @Log(title = "分销用户", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-user:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody BrokerageUserBo bo) {
        return R.ok(brokerageUserService.saveOrUpdate(bo));
    }

    /**
     * 删除分销用户。
     */
    @Log(title = "分销用户", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:brokerage-user:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(brokerageUserService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
