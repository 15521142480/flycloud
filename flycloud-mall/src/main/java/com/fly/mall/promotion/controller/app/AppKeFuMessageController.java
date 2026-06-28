package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.KeFuMessageBo;
import com.fly.mall.api.domain.promotion.vo.KeFuMessageVo;
import com.fly.mall.promotion.service.IKeFuMessageService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 客服消息 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/ke-fu-message")
public class AppKeFuMessageController {

    private final IKeFuMessageService keFuMessageService;

    /**
     * 查询移动端客服消息分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<KeFuMessageVo>> list(KeFuMessageBo bo, PageBo page) {
        return R.ok(keFuMessageService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<KeFuMessageVo>> page(KeFuMessageBo bo, PageBo page) {
        return R.ok(keFuMessageService.queryPageList(bo, page));
    }

    /**
     * 获取移动端客服消息详情。
     */
    @GetMapping("/get/{id}")
    public R<KeFuMessageVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(keFuMessageService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<KeFuMessageVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(keFuMessageService.queryById(id));
    }

}
